package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.xml
import java.io.EOFException
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author lukas
 * @since 15.10.19
 */
class Request(private val config: CardmarketApiConfiguration,
              private val method: Method,
              private val path: String,
              private var params: Map<String, *> = emptyMap<String, String>(),
              private var body: Node? = null) {
    val logger: Logger = LogManager.getLogger(javaClass)
    val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .registerModule(JavaTimeModule())

    fun params(vararg params: Pair<String, *>) = apply { this.params = params.filter { it.second != null }.toMap() }

    fun body(body: (Node.() -> Unit)?) = apply { this.body = xml("request", init = body) }

    inline fun <reified T : Any> submit() = perform { mapper.readValue(it, jacksonTypeRef<T>()) }

    inline fun <reified T : Any> submit(responseKey: String) = submit { mapper, map -> mapper.convertValue(map[responseKey], jacksonTypeRef<T>()) }

    inline fun <reified T : Any> submit(crossinline resultExtractor: (ObjectMapper, Map<String, Any>) -> T?) = perform {
        val map: Map<String, Any> = mapper.readValue(it, jacksonTypeRef<Map<String, Any>>())
        if(map.containsKey("errors")) {
            logger.warn(map["errors"])
        }
        resultExtractor.invoke(mapper, map)
    }

    fun <T : Any> perform(resultExtractor: (String) -> T?): T? {
        val baseUrl = config.url + "/ws/v2.0/output.json/" + path;
        val url = if (params.isNotEmpty()) params.entries.joinToString(prefix = "$baseUrl?", separator = "&") { "${it.key}=${encode(it.value.toString())}" } else baseUrl
        val (_, response, result) = method.fuel.invoke(url).also { request ->
            body?.let { request.body(toXml(it), Charsets.UTF_8) }
        }.header("Authorization", createOauthHeader(baseUrl, method, params))
                .responseString()
        logger.debug("Call: ${response["X-Request-Limit-Count"]}/${response["X-Request-Limit-Max"]}")
        return result.fold({
            logger.trace(it)
            when (it.length) {
                0 -> null
                else -> resultExtractor.invoke(it)
            }
        }, {
            if (it.cause?.cause !is EOFException) {
                logger.warn(it)
            }
            null
        })
    }

    private fun toXml(content: Node): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + content.toString(false)
    }

    private fun encode(s: String): String = URLEncoder.encode(s, Charsets.UTF_8.name()).replace("+", "%20").replace("'", "%27")

    private fun createOauthHeader(url: String, method: Method, queryParams: Map<String, *>): String {
        val timestamp = "" + (System.currentTimeMillis() / 1000)
        val headerParams = mapOf("oauth_consumer_key" to config.appToken,
                "oauth_token" to config.accessToken,
                "oauth_nonce" to "" + System.currentTimeMillis(),
                "oauth_timestamp" to timestamp,
                "oauth_signature_method" to "HMAC-SHA1",
                "oauth_version" to "1.0",
                "realm" to url).plus(queryParams).toSortedMap()
        val paramString = encode(headerParams.filter { it.key != "realm" }.map { "${it.key}=${encode(it.value.toString())}" }.joinToString("&"))
        val mac = Mac.getInstance("HmacSHA1")
        val signatureKey = encode(config.appSecret) + "&" + encode(config.accessTokenSecret)
        val secret = SecretKeySpec(signatureKey.toByteArray(), mac.algorithm)
        mac.init(secret)
        headerParams["oauth_signature"] = Base64.getEncoder().encodeToString(mac.doFinal("${method.name}&${encode(url)}&$paramString".toByteArray()))
        val authHeader = "OAuth " + headerParams.map { "${it.key}=\"${it.value}\"" }.joinToString(separator = ", ")
        logger.trace("authHeader: $authHeader")
        return authHeader
    }
}

enum class Method(val fuel: (String) -> Request) {
    GET({ Fuel.get(it) }),
    PUT({ Fuel.put(it) }),
    POST({ Fuel.post(it) }),
    DELETE({ Fuel.delete(it) })
}

fun Node.element(name: String, value: Any) = element(name, value.toString())
fun Node.element(name: String, value: Any?) {
    if (value != null) element(name, value)
}
package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

/**
 * @author lukas
 * @since 30.09.19
 */
const val GET = "GET"
const val PUT = "PUT"
const val POST = "POST"
const val DELETE = "DELETE"

abstract class AbstractService(private val config: CardmarketApiConfiguration) {
    private val apiUrl = "${config.url}/ws/v2.0/output.json/"
    private val logger: Logger = LogManager.getLogger(MarketplaceService::class.java)
    private val signatureKey = encode(config.appSecret) + "&" + encode(config.accessTokenSecret)

    val mapper : ObjectMapper = ObjectMapper().registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            //.also { it.configOverride(List::class.java).setterInfo = JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY) }

    fun toXml(vararg content: Pair<String, *>): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + toXmlImpl("request" to content)
    }

    private fun toXmlImpl(vararg content: Pair<*, *>): String {
        return content.joinToString {
            @Suppress("UNCHECKED_CAST")
            "<${it.first}>" + (when {
                it.second is Array<*> -> toXmlImpl(*(it.second as Array<Pair<*, *>>))
                it.second is Pair<*, *> -> toXmlImpl(it.second as Pair<*, *>)
                else -> it.second
            }) + "</${it.first}>\n"
        }
    }

    protected inline fun <reified T : Any> get(path: String, responseKey: String?): T? = performRequest(path, responseKey, GET) { Fuel.get(it) }

    protected fun <T : Any> getCustom(path: String, resultExtractor: (String) -> T?): T? =
            doPerformRequest(path, GET, { Fuel.get(it) }, resultExtractor)

    protected inline fun <reified T : Any> put(path: String, responseKey: String?): T? = performRequest(path, responseKey, PUT) { Fuel.put(it) }

    protected inline fun <reified T : Any> put(path: String, responseKey: String?, vararg body: Pair<String, *>): T? = performRequest(path, responseKey, PUT) { Fuel.put(it).body(toXml(*body)) }

    protected fun post(path: String, vararg body: Pair<String, *>): Unit = performRequest(path, null, POST) { Fuel.post(it).body(toXml(*body)) } ?: Unit

    protected fun delete(path: String): Unit = performRequest(path, null, DELETE) { Fuel.delete(it) } ?: Unit

    protected fun delete(path: String, vararg body: Pair<String, *>): Unit = performRequest(path, null, DELETE) { Fuel.delete(it).body(toXml(*body)) } ?: Unit

    inline fun <reified T : Any> performRequest(path: String, responseKey: String?, method: String, noinline requester: (String) -> Request): T? {
        return when {
            responseKey != null -> doPerformRequest(path, method, requester, {
                val map: Map<String, Any> = mapper.readValue(it, jacksonTypeRef<Map<String, Any>>())
                mapper.convertValue(map[responseKey], jacksonTypeRef<T>())
            })
            else -> doPerformRequest(path, method, requester, { mapper.readValue(it, jacksonTypeRef<T>()) })
        }
    }

    fun <T : Any> doPerformRequest(path: String, method: String, requester: (String) -> Request, resultExtractor: (String) -> T?): T? {
        val url = apiUrl + path
        val (_, response, result) = requester.invoke(url)
                .header("Authorization", createOauthHeader(url, method)).responseString()
        val limit = response["X-Request-Limit-Max"]
        val count = response["X-Request-Limit-Count"]
        logger.debug("Call: $count/$limit")
        return result.fold({
            logger.trace(it)
            when (it.length) {
                0 -> null
                else -> resultExtractor.invoke(it)
            }
        }, {
            logger.warn(it)
            null
        })
    }

    private fun encode(s: String): String = URLEncoder.encode(s, Charsets.UTF_8.name()).replace("+", "%20")

    private fun createOauthHeader(url: String, method: String): String {
        val timestamp = "" + (System.currentTimeMillis() / 1000)
        val index = url.indexOf('?')
        val baseUri = if (index > 0) url.substring(0, index) else url
        val headerParams = mutableMapOf("oauth_consumer_key" to config.appToken,
                "oauth_token" to config.accessToken,
                "oauth_nonce" to "" + System.currentTimeMillis(),
                "oauth_timestamp" to timestamp,
                "oauth_signature_method" to "HMAC-SHA1",
                "oauth_version" to "1.0",
                "realm" to baseUri).toSortedMap()
        var baseString = method.toUpperCase() + "&" + encode(baseUri) + "&"
        if (index > 0) {
            val urlParams = url.substring(index + 1)
            val args = parseQueryString(urlParams)
            for (k in args.entries) {
                headerParams[k.key] = k.value
            }
        }
        val encodedParams = TreeMap<String, String>()
        for (k in headerParams.entries) {
            if (k.key != "realm") {
                encodedParams[encode(k.key)] = encode(k.value)
            }
        }
        val paramStrings = ArrayList<String>()
        for (parameter in encodedParams.entries) {
            paramStrings.add(parameter.key + "=" + parameter.value)
        }
        val paramString = encode(paramStrings.joinToString("&")).replace("'", "%27")
        baseString += paramString
        val mac = Mac.getInstance("HmacSHA1")
        val secret = SecretKeySpec(signatureKey.toByteArray(), mac.algorithm)
        mac.init(secret)
        val digest = mac.doFinal(baseString.toByteArray())
        val oAuthSignature = Base64.getEncoder().encodeToString(digest)
        headerParams["oauth_signature"] = oAuthSignature
        val headerParamStrings = ArrayList<String>()
        for (parameter in headerParams.entries) {
            headerParamStrings.add(parameter.key + "=\"" + parameter.value + "\"")
        }
        val authHeader = "OAuth " + headerParamStrings.joinToString(separator = ", ")
        logger.trace("authHeader: $authHeader")
        return authHeader
    }

    private fun parseQueryString(query: String): Map<String, String> {
        val queryParameters = TreeMap<String, String>()
        val querySegments = query.split(("&").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (segment in querySegments) {
            val parts = segment.split(("=").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.isNotEmpty()) {
                val key = parts[0].replace(("\\?").toRegex(), " ").trim { it <= ' ' }
                val `val` = parts[1].trim { it <= ' ' }
                queryParameters[key] = `val`
            }
        }
        return queryParameters
    }
}
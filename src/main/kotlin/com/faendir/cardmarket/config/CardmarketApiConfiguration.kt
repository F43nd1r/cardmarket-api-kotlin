package com.faendir.cardmarket.config

import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import org.apache.logging.log4j.core.util.NullOutputStream
import java.io.File

/**
 * @author lukas
 * @since 30.09.19
 */
data class CardmarketApiConfiguration(val appToken: String, val appSecret: String, val accessToken: String, val accessTokenSecret: String, val url: String) {
    companion object {
        private val app_token by stringType
        private val app_secret by stringType
        private val access_token by stringType
        private val access_token_secret by stringType
        private val url by stringType
        fun get(commandlineArgs: Array<String> = emptyArray()): CardmarketApiConfiguration {
            val config = parseArgs(commandlineArgs, helpOutput = NullOutputStream.getInstance(), helpExit = { throw HelpExit() }).first overriding
                    systemProperties() overriding
                    EnvironmentVariables() overriding
                    ConfigurationProperties.fromOptionalFile(File("cardmarket.properties")) overriding
                    ConfigurationProperties.fromResource("default.properties")
            return CardmarketApiConfiguration(config[app_token], config[app_secret], config[access_token], config[access_token_secret], config[url])
        }

        private class HelpExit : Exception()
    }
}
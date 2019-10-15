package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.time.format.DateTimeFormatter

/**
 * @author lukas
 * @since 30.09.19
 */
abstract class AbstractService(private val config: CardmarketApiConfiguration) {
    private val logger: Logger = LogManager.getLogger(javaClass)

    val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZ")

    fun get(path: String) = Request(config, Method.GET, path)

    fun post(path: String) = Request(config, Method.POST, path)

    fun put(path: String) = Request(config, Method.PUT, path)

    fun delete(path: String) = Request(config, Method.DELETE, path)
}
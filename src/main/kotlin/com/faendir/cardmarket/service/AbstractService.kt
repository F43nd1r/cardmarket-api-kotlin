package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import java.time.format.DateTimeFormatter

/**
 * @author lukas
 * @since 30.09.19
 */
abstract class AbstractService(private val config: CardmarketApiConfiguration) {

    internal val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZ")

    internal fun get(path: String) = Request(config, Method.GET, path)

    internal fun post(path: String) = Request(config, Method.POST, path)

    internal fun put(path: String) = Request(config, Method.PUT, path)

    internal fun delete(path: String) = Request(config, Method.DELETE, path)
}
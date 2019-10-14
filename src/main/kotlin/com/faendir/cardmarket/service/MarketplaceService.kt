@file:Suppress("unused")

package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

/**
 * @author lukas
 * @since 30.09.19
 */
class MarketplaceService(config: CardmarketApiConfiguration) : AbstractService(config) {

    fun getGames(): List<Game> = get("games", "game") ?: emptyList()

    fun getExpansions(idGame: Int): List<Expansion> = get("games/$idGame/expansions", "expansion") ?: emptyList()

    fun getProductsByExpansion(idExpansion: Int): List<Product> = get("expansions/$idExpansion/singles", "single") ?: emptyList()

    fun getProduct(id: Int): ProductDetails? = get("products/$id", "product")

    fun findProduct(search: String, exact: Boolean? = null, idGame: Int? = null, idLanguage: Int? = null, partial: Pair<Int, Int>? = null): List<Product> =
            get("products/find?" + mapOf("search" to search,
                    "exact" to exact,
                    "idGame" to idGame,
                    "idLanguage" to idLanguage,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "product") ?: emptyList()

    fun findArticles(product: Product, userType: UserType? = null, minUserScore: Reputation? = null, idLanguage: Int? = null, minCondition: Condition? = null, isFoil: Boolean? = null,
                     isSigned: Boolean? = null, isAltered: Boolean? = null, minAvailable: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("articles/${product.idProduct}?" + mapOf("userType" to userType,
                    "minUserScore" to minUserScore,
                    "idLanguage" to idLanguage,
                    "minCondition" to minCondition,
                    "isFoil" to isFoil,
                    "isSigned" to isSigned,
                    "isAltered" to isAltered,
                    "minAvailable" to minAvailable,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "article") ?: emptyList()

    fun getMetaproduct(id: Int): Metaproduct? = getCustom("metaproducts/$id") {
        val map = mapper.readValue(it, jacksonTypeRef<Map<String, Any>>())
        val metaproduct : Metaproduct? = mapper.convertValue(map["metaproduct"], jacksonTypeRef<Metaproduct>())
        metaproduct?.copy(products = mapper.convertValue(map["product"], jacksonTypeRef<List<Product>>()))
    }

    fun findMetaproducts(search: String, exact: Boolean? = null, idGame: Int? = null, idLanguage: Int? = null): List<Metaproduct> =
            get("metaproducts/find?" + mapOf("search" to search,
                    "exact" to exact,
                    "idGame" to idGame,
                    "idLanguage" to idLanguage).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "metaproduct") ?: emptyList()

    fun getUser(id: Int): User? = get("users/$id", "user")

    fun getUser(name: String): User? = get("users/$name", "user")

    fun findUsers(name: String): List<User> = get("users/find?search=$name", "user") ?: emptyList()

    fun getUserArticles(idUser: Int, idGame: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("users/$idUser/articles?" + mapOf(
                    "idGame" to idGame,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "article") ?: emptyList()

    fun getUserArticles(idUser: String, idGame: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("users/$idUser/articles?" + mapOf(
                    "idGame" to idGame,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "article") ?: emptyList()
}


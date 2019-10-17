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

    fun getGames(): List<Game> = get("games").submit("game") ?: emptyList()

    fun getExpansions(game: Game): List<Expansion> = get("games/${game.idGame}/expansions").submit("expansion") ?: emptyList()

    fun getProductsByExpansion(expansion: Expansion): List<Product> = get("expansions/${expansion.idExpansion}/singles").submit("single") ?: emptyList()

    fun getProductDetails(product: HasProductId): ProductDetails = get("products/${product.idProduct}").submit("product")!!

    fun findProduct(search: String, exact: Boolean? = null, idGame: Int? = null, idLanguage: Int? = null, partial: Pair<Int, Int>? = null): List<Product> =
            get("products/find").params("search" to search,
                    "exact" to exact,
                    "idGame" to idGame,
                    "idLanguage" to idLanguage,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).submit("product") ?: emptyList()

    fun findArticles(product: HasProductId, userType: UserType? = null, minUserScore: Reputation? = null, idLanguage: Int? = null, minCondition: Condition? = null, isFoil: Boolean? = null,
                     isSigned: Boolean? = null, isAltered: Boolean? = null, minAvailable: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("articles/${product.idProduct}").params("userType" to userType,
                    "minUserScore" to minUserScore,
                    "idLanguage" to idLanguage,
                    "minCondition" to minCondition,
                    "isFoil" to isFoil,
                    "isSigned" to isSigned,
                    "isAltered" to isAltered,
                    "minAvailable" to minAvailable,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).submit("article") ?: emptyList()

    fun getMetaproduct(product: HasMetaproductId): Metaproduct = get("metaproducts/${product.idMetaproduct}").submit { mapper, map ->
        val metaproduct: Metaproduct? = mapper.convertValue(map["metaproduct"], jacksonTypeRef<Metaproduct>())
        metaproduct?.copy(products = mapper.convertValue(map["product"], jacksonTypeRef<List<Product>>()))
    }!!

    fun findMetaproducts(search: String, exact: Boolean? = null, idGame: Int? = null, idLanguage: Int? = null): List<Metaproduct> =
            get("metaproducts/find").params("search" to search,
                    "exact" to exact,
                    "idGame" to idGame,
                    "idLanguage" to idLanguage).submit { mapper, map ->
                val list = mapper.convertValue(map["metaproduct"], jacksonTypeRef<List<Map<String, Any>>>())
                list.mapNotNull {
                    val metaproduct: Metaproduct? = mapper.convertValue(it["metaproduct"], jacksonTypeRef<Metaproduct>())
                    metaproduct?.copy(products = mapper.convertValue(it["product"], jacksonTypeRef<List<Product>>()))
                }
            } ?: emptyList()

    fun getUser(id: Int): User? = get("users/$id").submit("user")

    fun getUser(name: String): User? = get("users/$name").submit("user")

    fun findUsers(name: String): List<User> = get("users/find").params("search" to name).submit("user") ?: emptyList()

    fun getUserArticles(user: HasUserId, idGame: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("users/${user.idUser}/articles").params(
                    "idGame" to idGame,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).submit("article") ?: emptyList()

    fun getUserArticles(userName: String, idGame: Int? = null, partial: Pair<Int, Int>? = null): List<Article> =
            get("users/$userName/articles").params(
                    "idGame" to idGame,
                    "start" to partial?.first,
                    "maxResults" to partial?.second).submit("article") ?: emptyList()
}


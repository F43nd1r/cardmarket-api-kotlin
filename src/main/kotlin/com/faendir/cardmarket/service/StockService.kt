package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Article
import com.faendir.cardmarket.model.Game

/**
 * @author lukas
 * @since 09.10.19
 */
class StockService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun get(start: Int? = null): List<Article> = get(if (start != null) "stock/$start" else "stock", "article") ?: emptyList()

    fun add(vararg articles: Article) = post("stock", *(articles.map {
        "article" to arrayOf(
                "idProduct" to it.idProduct,
                "idLanguage" to it.language.idLanguage,
                "comments" to it.comments,
                "count" to it.count,
                "price" to it.price,
                "condition" to it.condition,
                "isFoil" to it.isFoil,
                "isSigned" to it.isSigned,
                "isPlayset" to it.isPlayset
        )
    }.toTypedArray()))

    fun update(vararg articles: Article): Unit = put("stock", null, *(articles.map {
        "article" to arrayOf(
                "idArticle" to it.idArticle,
                "idLanguage" to it.language.idLanguage,
                "comments" to it.comments,
                "count" to it.count,
                "price" to it.price,
                "condition" to it.condition,
                "isFoil" to it.isFoil,
                "isSigned" to it.isSigned,
                "isPlayset" to it.isPlayset
        )
    }.toTypedArray())) ?: Unit

    fun remove(vararg articles: Article): Unit = delete("stock", *(articles.map {
        "article" to arrayOf(
                "idArticle" to it.idArticle,
                "count" to it.count
        )
    }.toTypedArray()))

    fun getInShoppingCarts(): List<Article> = get("stock/shoppingcart-articles", "article") ?: emptyList()

    fun getArticle(id: Int): Article? = get("stock/$id", "article")

    fun findArticle(name: String, game: Game): List<Article> = get("stock/$name/${game.idGame}", "article") ?: emptyList()

    fun increaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/increase", null, *(articles.map {
        "article" to arrayOf(
                "idArticle" to it.first.idArticle,
                "count" to it.second
        )
    }.toTypedArray())) ?: Unit

    fun decreaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/decrease", null, *(articles.map {
        "article" to arrayOf(
                "idArticle" to it.first.idArticle,
                "count" to it.second
        )
    }.toTypedArray())) ?: Unit
}
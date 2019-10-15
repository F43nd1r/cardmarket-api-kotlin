package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Article
import com.faendir.cardmarket.model.Game
import com.faendir.cardmarket.util.leafs
import com.faendir.cardmarket.util.tree

/**
 * @author lukas
 * @since 09.10.19
 */
class StockService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun get(start: Int? = null): List<Article> = get(if (start != null) "stock/$start" else "stock").submit("article") ?: emptyList()

    fun add(vararg articles: Article): Unit = post("stock").body(*(articles.map {
        tree("article") {
            node(leafs("idProduct", it.idProduct.toString()))
            node(leafs("idLanguage", it.language.idLanguage.toString()))
            node(leafs("comments", it.comments))
            node(leafs("count", it.count.toString()))
            node(leafs("price", it.price.toString()))
            node(leafs("condition", it.condition.toString()))
            node(leafs("isFoil", it.isFoil.toString()))
            node(leafs("isSigned", it.isSigned.toString()))
            node(leafs("isPlayset", it.isPlayset.toString()))
        }
    }.toTypedArray())).submit() ?: Unit

    fun update(vararg articles: Article): Unit = put("stock").body(*(articles.map {
        tree<String>("article") {
            node(leafs("idArticle", it.idArticle.toString()))
            node(leafs("idLanguage", it.language.idLanguage.toString()))
            node(leafs("comments", it.comments))
            node(leafs("count", it.count.toString()))
            node(leafs("price", it.price.toString()))
            node(leafs("condition", it.condition.toString()))
            node(leafs("isFoil", it.isFoil.toString()))
            node(leafs("isSigned", it.isSigned.toString()))
            node(leafs("isPlayset", it.isPlayset.toString()))
        }
    }.toTypedArray())).submit() ?: Unit

    fun remove(vararg articles: Article): Unit = delete("stock").body(*(articles.map {
        tree("article") {
            node(leafs("idArticle", it.idArticle.toString()))
            node(leafs("count", it.count.toString()))
        }
    }.toTypedArray())).submit() ?: Unit

    fun getInShoppingCarts(): List<Article> = get("stock/shoppingcart-articles").submit("article") ?: emptyList()

    fun getArticle(id: Int): Article? = get("stock/$id").submit("article")

    fun findArticle(name: String, game: Game): List<Article> = get("stock/$name/${game.idGame}").submit("article") ?: emptyList()

    fun increaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/increase").body(*(articles.map {
        tree("article") {
            node(leafs("idArticle", it.first.idArticle.toString()))
            node(leafs("count", it.second.toString()))
        }
    }.toTypedArray())).submit() ?: Unit

    fun decreaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/decrease").body(*(articles.map {
        tree("article") {
            node(leafs("idArticle", it.first.idArticle.toString()))
            node(leafs("count", it.second.toString()))
        }
    }.toTypedArray())).submit() ?: Unit
}

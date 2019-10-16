package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Article
import com.faendir.cardmarket.model.Game
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef

/**
 * @author lukas
 * @since 09.10.19
 */
class StockService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun get(start: Int? = null): List<Article> = get(if (start != null) "stock/$start" else "stock").submit("article") ?: emptyList()

    fun add(vararg articles: Article): List<Article> = post("stock").body {
        articles.forEach {
            "article" {
                element("idProduct", it.idProduct)
                element("idLanguage", it.language.idLanguage)
                element("comments", it.comments)
                element("count", it.count)
                element("price", it.price)
                element("condition", it.condition)
                element("isFoil", it.isFoil)
                element("isSigned", it.isSigned)
                element("isPlayset", it.isPlayset)
            }
        }
    }.submit{mapper, map ->
        val list = mapper.convertValue(map["inserted"], jacksonTypeRef<List<Map<String,Any>>>())
        list.map { mapper.convertValue(it["idArticle"], jacksonTypeRef<Article>()) }
    } ?: emptyList()

    fun update(vararg articles: Article): Unit = put("stock").body {
        articles.forEach {
            "article" {
                element("idArticle", it.idArticle)
                element("idLanguage", it.language.idLanguage)
                element("comments", it.comments)
                element("count", it.count)
                element("price", it.price)
                element("condition", it.condition)
                element("isFoil", it.isFoil)
                element("isSigned", it.isSigned)
                element("isPlayset", it.isPlayset)
            }
        }
    }.submit() ?: Unit

    fun remove(vararg articles: Article): Unit = delete("stock").body {
        articles.forEach {
            "article" {
                element("idArticle", it.idArticle)
                element("count", it.count)
            }
        }
    }.submit() ?: Unit

    fun getInShoppingCarts(): List<Article> = get("stock/shoppingcart-articles").submit("article") ?: emptyList()

    fun getArticle(id: Int): Article? = get("stock/$id").submit("article")

    fun findArticle(name: String, game: Game): List<Article> = get("stock/$name/${game.idGame}").submit("article") ?: emptyList()

    fun increaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/increase").body {
        articles.forEach {
            "article" {
                element("idArticle", it.first.idArticle)
                element("count", it.second)
            }
        }
    }.submit() ?: Unit

    fun decreaseCount(vararg articles: Pair<Article, Int>): Unit = put("stock/decrease").body {
        articles.forEach {
            "article" {
                element("idArticle", it.first.idArticle)
                element("count", it.second)
            }
        }
    }.submit() ?: Unit
}

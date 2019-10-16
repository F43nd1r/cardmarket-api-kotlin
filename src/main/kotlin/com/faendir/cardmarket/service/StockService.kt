package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Article
import com.faendir.cardmarket.model.Game
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * @author lukas
 * @since 09.10.19
 */
class StockService(config: CardmarketApiConfiguration) : AbstractService(config) {
    val logger: Logger = LogManager.getLogger(javaClass)
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
    }.submit { mapper, map ->
        val list = mapper.convertValue(map["inserted"], jacksonTypeRef<List<Map<String, Any>>>())
        list.map {
            if (it.containsKey("error")) {
                logger.warn("error: " + it["error"])
            }
            mapper.convertValue(it["idArticle"], jacksonTypeRef<Article>())
        }
    } ?: emptyList()

    fun update(vararg articles: Article): List<Article> = put("stock").body {
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
    }.submit { mapper, map ->
        mapper.convertValue(map["notUpdatedArticles"], jacksonTypeRef<List<Map<String, Any>>>()).forEach { logger.warn("Could not update article " + it["idArticle"]) }
        val list = mapper.convertValue(map["updatedArticles"], jacksonTypeRef<List<Map<String, Any>>>())
        list.map { mapper.convertValue(it, jacksonTypeRef<Article>()) }
    } ?: emptyList()

    fun remove(vararg articles: Article): Unit = delete("stock").body {
        articles.forEach {
            "article" {
                element("idArticle", it.idArticle)
                element("count", it.count)
            }
        }
    }.submit() ?: Unit

    fun getInShoppingCarts(): List<Article> = get("stock/shoppingcart-articles").submit("article") ?: emptyList()

    fun getArticle(id: Int): Article? = get("stock/article/$id").submit("article")

    fun findArticle(name: String, game: Game): List<Article> = get("stock/articles/${encode(name)}/${game.idGame}").submit("article") ?: emptyList()

    fun increaseCount(vararg articles: Pair<Article, Int>): List<Article> = put("stock/increase").body {
        articles.forEach {
            "article" {
                element("idArticle", it.first.idArticle)
                element("count", it.second)
            }
        }
    }.submit("article") ?: emptyList()

    fun decreaseCount(vararg articles: Pair<Article, Int>): List<Article> = put("stock/decrease").body {
        articles.forEach {
            "article" {
                element("idArticle", it.first.idArticle)
                element("count", it.second)
            }
        }
    }.submit("article") ?: emptyList()
}

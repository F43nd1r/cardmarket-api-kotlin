package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Article
import com.faendir.cardmarket.model.Condition
import com.faendir.cardmarket.model.Game
import com.faendir.cardmarket.model.Language
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * @author lukas
 * @since 16.10.19
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class StockServiceTest {
    private val config = CardmarketApiConfiguration.get()
    private val service = StockService(config)
    private val marketplaceService = MarketplaceService(config)
    @Test
    fun test() {
        val product1 = marketplaceService.findProduct("Bridge from Below")[0]
        val product2 = marketplaceService.findProduct("Crucible of Worlds")[0]
        val articles = service.add(
                Article(0, product1.idProduct, Language.ENGLISH, "", 10.0, 1, false, null, Condition.NEAR_MINT, isFoil = false, isSigned = false, isAltered = false, isPlayset = false),
                        Article(0, product2.idProduct, Language.ENGLISH, "Test", 20.0, 1, false, null, Condition.NEAR_MINT, isFoil = true, isSigned = false, isAltered = false, isPlayset = false)
        )
        assertThat(articles, hasSize(equalTo(2)))
        assertThat(articles, allElements(isIn(service.get())))
        val newArticles = service.update(*articles.map { it.copy(isSigned = true) }.toTypedArray())
        assertThat(newArticles, hasSize(equalTo(2)))
        assertThat(newArticles, allElements(has(Article::isSigned, equalTo(true))))
        service.remove(articles[0])
        assertThat(service.get(), !anyElement(equalTo(articles[0])))

        //no tests possible on this?
        service.getInShoppingCarts()

        val article = service.getArticle(newArticles[1].idArticle)!!
        assertThat(article, equalTo(newArticles[1]))
        val find = service.findArticle("Crucible of Worlds", Game.MTG)[0]
        assertThat(find, equalTo(newArticles[1]))
        service.increaseCount(article to 2)
        assertThat(service.getArticle(article.idArticle)!!, has(Article::count, equalTo(3)))
        service.decreaseCount(article to 1)
        val lastArticle = service.getArticle(article.idArticle)!!
        assertThat(lastArticle, has(Article::count, equalTo(2)))
        service.remove(lastArticle)
    }
}
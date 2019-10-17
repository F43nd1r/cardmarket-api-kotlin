package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * @author lukas
 * @since 16.10.19
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MarketplaceServiceTest {
    private val config = CardmarketApiConfiguration.get()
    private val service = MarketplaceService(config)
    private val stockService = StockService(config)
    private val name = "Crucible of Worlds"
    private val prints = 6

    @Test
    fun games() {
        assertThat(service.getGames(), anyElement(equalTo(Game.MTG)))
    }

    @Test
    fun expansions() {
        assertThat(service.getExpansions(Game.MTG), anyElement(has(Expansion::enName, equalTo("Core 2019"))))
    }

    @Test
    fun expansionSingles() {
        val expansion = service.getExpansions(Game.MTG).find { it.enName == "Core 2019" }!!
        assertThat(service.getProductsByExpansion(expansion), anyElement(has(Product::enName, equalTo(name))))
    }

    @Test
    fun products() {
        val product = service.findProduct(name)[0]
        assertThat(product, has(Product::enName, equalTo(name)))
        val productDetails = service.getProductDetails(product)
        assertThat(productDetails, has(ProductDetails::enName, equalTo(name)))
        assertThat(productDetails, has(ProductDetails::reprints, hasSize(equalTo(prints - 1))))
    }

    @Test
    fun articles() {
        val product = service.findProduct(name)[0]
        val findArticles = service.findArticles(product, isFoil = true, partial = 0 to 100)
        assertThat(findArticles, !isEmpty)
        assertThat(findArticles, allElements(has(Article::idProduct, equalTo(product.idProduct))))
        assertThat(findArticles, allElements(has(Article::isFoil, equalTo(true))))
    }

    @Test
    fun metaproduct() {
        val product = service.findProduct(name)[0]
        val metaproduct = service.getMetaproduct(product)
        assertThat(metaproduct, has(Metaproduct::enName, equalTo(name)))
        assertThat(metaproduct, has(Metaproduct::products, hasSize(equalTo(prints))))
        val metaproducts = service.findMetaproducts(name)
        assertThat(metaproducts, anyElement(equalTo(metaproduct)))
    }

    @Test
    fun user() {
        assertThat(service.getUser(65310125), present())
        assertThat(service.getUser("KotlinTest"), present())
    }

    @Test
    fun userArticles() {
        val product = service.findProduct(name)[0]
        val article = stockService.add(Article(0, product.idProduct, Language.ENGLISH, "", 10.0, 1, false, null, Condition.NEAR_MINT, isFoil = false, isSigned = false, isAltered = false, isPlayset = false))[0]
        assertThat(service.getUserArticles(object : HasUserId {
            override val idUser = 65310125
        }), anyElement(has(Article::idProduct, equalTo(product.idProduct))))
        assertThat(service.getUserArticles("KotlinTest"), anyElement(has(Article::idProduct, equalTo(product.idProduct))))
        stockService.remove(article)
    }
}
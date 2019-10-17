package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Address
import com.faendir.cardmarket.model.Order
import com.faendir.cardmarket.model.Reservation
import com.faendir.cardmarket.model.ShoppingCart
import com.natpryce.hamkrest.anyElement
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

/**
 * @author lukas
 * @since 16.10.19
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ShoppingCartServiceTest {
    private val config = CardmarketApiConfiguration.get()
    private val service = ShoppingCartService(config)
    private val marketplaceService = MarketplaceService(config)

    @Test
    fun checkout() {
        val article = marketplaceService.findArticles(marketplaceService.findProduct("Vine Mare")[0], partial = 0 to 100)[Random.nextInt(100)]
        service.addArticles(article)
        val cart = service.getShoppingCart()
        assertThat(cart, has(ShoppingCart::reservations, anyElement(has(Reservation::article, anyElement(equalTo(article))))))
        val order = service.checkout()[0]
        assertThat(order, has(Order::isBuyer, equalTo(true)))
        assertThat(order, has(Order::article, anyElement(equalTo(article))))
    }

    @Test
    fun remove() {
        val articles = marketplaceService.findArticles(marketplaceService.findProduct("Thorn Lieutenant")[0], partial = 0 to 2)
        service.addArticles(*articles.toTypedArray())
        var cart = service.getShoppingCart()
        assertThat(cart, has(ShoppingCart::reservations, anyElement(has(Reservation::article, anyElement(equalTo(articles[0]))))))
        assertThat(cart, has(ShoppingCart::reservations, anyElement(has(Reservation::article, anyElement(equalTo(articles[1]))))))
        service.removeArticles(articles[0])
        cart = service.getShoppingCart()
        assertThat(cart, has(ShoppingCart::reservations, !anyElement(has(Reservation::article, anyElement(equalTo(articles[0]))))))
        assertThat(cart, has(ShoppingCart::reservations, anyElement(has(Reservation::article, anyElement(equalTo(articles[1]))))))
        service.emptyShoppingCart()
        assertThat(service.getShoppingCart().reservations,  isEmpty)
    }

    @Test
    fun shippingAddress() {
        val article = marketplaceService.findArticles(marketplaceService.findProduct("Bomat Courier")[0], partial = 0 to 1)[0]
        service.addArticles(article)
        val address = Address("Max Mustermann", null, "Müllerweg 2", "10115", "Berlin", "D")
        service.setShippingAddress(address)
        assertThat(service.getShoppingCart(), has(ShoppingCart::shippingAddress, equalTo(address)))
        service.setShippingAddress(Address("Max Mustermann", null, "Müllerweg 1", "10115", "Berlin", "D"))
        assertThat(service.getShoppingCart(), has(ShoppingCart::shippingAddress, !equalTo(address)))
        service.emptyShoppingCart()
    }

    @Test
    fun shippingMethods() {
        val article = marketplaceService.findArticles(marketplaceService.findProduct("Sai, Master Thopterist")[0], partial = 0 to 1)[0]
        service.addArticles(article)
        val reservation = service.getShoppingCart().reservations[0]
        val methods = service.getShippingMethods(reservation)
        assertThat(methods, !isEmpty)
        val method = methods.find { it != reservation.shippingMethod }!!
        service.setShippingMethod(reservation, method)
        assertThat(service.getShoppingCart().reservations[0].shippingMethod, equalTo(method))
        service.emptyShoppingCart()
    }
}
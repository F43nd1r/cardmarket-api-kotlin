package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*

/**
 * @author lukas
 * @since 03.10.19
 */
class ShoppingCartService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getShoppingCart(): ShoppingCart? = get("shoppingcart").submit()

    fun addArticles(vararg articles: Article): ShoppingCart? =
            put("shoppingcart").body {
                element("action", "add")
                articles.forEach {
                    "article" {
                        element("idArticle", it.idArticle)
                        element("count", it.count)
                    }
                }
            }.submit()

    fun checkout(): Order? = put("shoppingcart/checkout").submit("order")

    fun setShippingAddress(address: Address): ShoppingCart? = put("shoppingcart/shippingaddress").body {
        element("name", address.name)
        element("extra", address.extra)
        element("street", address.street)
        element("zip", address.zip)
        element("city", address.city)
        element("country", address.country)
    }.submit()

    fun getShippingMethods(reservation: Reservation): List<ShippingMethod> = get("shoppingcart/shippingmethod/${reservation.idReservation}").submit("shippingMethod") ?: emptyList()

    fun setShippingMethod(reservation: Reservation, shippingMethod: ShippingMethod): ShoppingCart? = put("shoppingcart/shippingmethod/${reservation.idReservation}")
            .body { element("idShippingMethod", shippingMethod.idShippingMethod) }.submit()
}
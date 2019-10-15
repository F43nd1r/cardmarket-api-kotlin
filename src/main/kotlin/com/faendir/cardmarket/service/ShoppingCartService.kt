package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*
import com.faendir.cardmarket.util.leafs
import com.faendir.cardmarket.util.tree

/**
 * @author lukas
 * @since 03.10.19
 */
class ShoppingCartService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getShoppingCart(): ShoppingCart? = get("shoppingcart").submit()

    fun addArticles(vararg articles: Article): ShoppingCart? =
            put("shoppingcart").body(leafs("action", "add"), *(articles.map {
                tree("article") {
                    node(leafs("idArticle", it.idArticle.toString()))
                    node(leafs("count", it.count.toString()))
                }
            }).toTypedArray()).submit()

    fun checkout(): Order? = put("shoppingcart/checkout").submit("order")

    fun setShippingAddress(address: Address): ShoppingCart? = put("shoppingcart/shippingaddress").body(
            *(listOf(leafs("name", address.name),
                    leafs("extra", address.extra),
                    leafs("street", address.street),
                    leafs("zip", address.zip),
                    leafs("city", address.city),
                    leafs("country", address.country)).filter { it.children.isNotEmpty() }.toTypedArray()))
            .submit()

    fun getShippingMethods(reservation: Reservation): List<ShippingMethod> = get("shoppingcart/shippingmethod/${reservation.idReservation}").submit("shippingMethod") ?: emptyList()

    fun setShippingMethod(reservation: Reservation, shippingMethod: ShippingMethod): ShoppingCart? = put("shoppingcart/shippingmethod/${reservation.idReservation}")
            .body(leafs("idShippingMethod", shippingMethod.idShippingMethod.toString())).submit()
}
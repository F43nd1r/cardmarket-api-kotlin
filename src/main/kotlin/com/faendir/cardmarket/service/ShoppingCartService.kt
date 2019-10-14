package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.*

/**
 * @author lukas
 * @since 03.10.19
 */
class ShoppingCartService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getShoppingCart(): ShoppingCart? = get("shoppingcart", null)

    fun addArticles(vararg articles: Article): ShoppingCart? =
            put("shoppingcart", null, "action" to "add", *(articles.map { "article" to arrayOf("idArticle" to it.idArticle, "count" to it.count) }.toTypedArray()))

    fun checkout(): Order? = put("shoppingcart/checkout", "order")

    fun setShippingAddress(address: Address): ShoppingCart? = put("shoppingcart/shippingaddress", null,
            "name" to address.name,
            "extra" to address.extra,
            "street" to address.street,
            "zip" to address.zip,
            "city" to address.city,
            "country" to address.country
    )

    fun getShippingMethods(reservation: Reservation): List<ShippingMethod> = get("shoppingcart/shippingmethod/${reservation.idReservation}", "shippingMethod") ?: emptyList()

    fun setShippingMethod(reservation: Reservation, shippingMethod: ShippingMethod): ShoppingCart? = put("shoppingcart/shippingmethod/${reservation.idReservation}", null,
            "idShippingMethod" to shippingMethod.idShippingMethod)
}
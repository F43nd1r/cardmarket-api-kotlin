package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author lukas
 * @since 03.10.19
 */
data class ShoppingCart(val account: Account?,
                        val shippingAddress: Address?,
                        @JsonProperty("shoppingCart")
                        val reservations: List<Reservation>)

data class Reservation(val idReservation: Int,
                       val isBuyer: Boolean,
                       val seller: User,
                       val article: List<Article>,
                       val articleValue: Double,
                       val articleCount: Int,
                       val shippingMethod: ShippingMethod,
                       val totalValue: Double) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idReservation == (other as Reservation).idReservation)

    override fun hashCode(): Int = idReservation
}


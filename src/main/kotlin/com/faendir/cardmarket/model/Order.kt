package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author lukas
 * @since 08.10.19
 */
data class Order(val idOrder: Int,
                 val isBuyer: Boolean,
                 val seller: User,
                 val buyer: User,
                 val state: State,
                 val shippingMethod: ShippingMethod,
                 val trackingNumber: String?,
                 val isPresale: Boolean,
                 val shippingAddress: Address,
                 val articleCount: Int,
                 val note: String?,
                 val evaluation: Evaluation?,
                 val article: List<Article>,
                 val articleValue: Double,
                 val serviceFeeValue: Double?,
                 val totalValue: Double) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idOrder == (other as Order).idOrder)

    override fun hashCode(): Int = idOrder
}

data class State(val state: States,
                 val dateBought: String?,
                 val datePaid: String?,
                 val dateSent: String?,
                 val dateReceived: String?,
                 val dateCanceled: String?,
                 val reason: String?,
                 val wasMergedInto: Int?)

enum class States(@JsonValue val value: String) {
    BOUGHT("bought"),
    PAID("paid"),
    SENT("sent"),
    RECEIVED("received"),
    EVALUATED("evaluated"),
    LOST("lost"),
    CANCELLED("cancelled")
}

data class Evaluation(val evaluationGrade: Int,
                      val itemDescription: Int,
                      val packaging: Int,
                      val speed: Int,
                      val comment: String?,
                      val complaint: List<String>)
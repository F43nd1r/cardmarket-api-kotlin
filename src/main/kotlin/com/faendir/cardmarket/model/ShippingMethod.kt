package com.faendir.cardmarket.model

data class ShippingMethod(val idShippingMethod: Int,
                          val name: String,
                          val price: Double,
                          val isLetter: Boolean,
                          val isInsured: Boolean) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idShippingMethod == (other as ShippingMethod).idShippingMethod)

    override fun hashCode(): Int = idShippingMethod
}
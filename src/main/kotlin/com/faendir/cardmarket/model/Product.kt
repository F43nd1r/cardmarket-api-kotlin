package com.faendir.cardmarket.model


/**
 * @author lukas
 * @since 01.10.19
 */
data class Product(override val idProduct: Int,
                   override val idMetaproduct: Int,
                   override val countReprints: Int,
                   override val enName: String,
                   override val localization: List<Localization>,
                   override val website: String,
                   override val image: String,
                   override val gameName: String,
                   override val categoryName: String,
                   override val number: String,
                   override val rarity: String,
                   val expansionName: String) : BaseProduct {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idProduct == (other as Product).idProduct)

    override fun hashCode(): Int = idProduct
}
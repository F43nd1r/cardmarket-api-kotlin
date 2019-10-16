package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author lukas
 * @since 01.10.19
 */
data class ProductDetails(override val idProduct: Int,
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
                          val expansion: ExpansionInfo,
                          val priceGuide: PriceGuide,
                          @JsonProperty("reprint")
                          val reprints: List<Reprint> = emptyList()) : BaseProduct {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idProduct == (other as ProductDetails).idProduct)

    override fun hashCode(): Int = idProduct
}

data class ExpansionInfo(val idExpansion: Int, val enName: String, val expansionIcon: Int)

data class PriceGuide(val SELL: Double, val LOW: Double, val LOWEX: Double, val LOWFOIL: Double, val AVG: Double, val TREND: Double, val TRENDFOIL: Double)

data class Reprint(val idProduct: Int, val expansion: String, val expansionIcon: Int)
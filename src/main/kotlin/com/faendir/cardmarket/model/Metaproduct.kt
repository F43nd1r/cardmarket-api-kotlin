package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author lukas
 * @since 01.10.19
 */
data class Metaproduct(override val idMetaproduct: Int,
                       val enName: String,
                       val localization: List<Localization> = emptyList(),
                       @JsonProperty("product")
                       val products: List<Product> = emptyList()) : HasMetaproductId {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idMetaproduct == (other as Metaproduct).idMetaproduct)

    override fun hashCode(): Int = idMetaproduct
}
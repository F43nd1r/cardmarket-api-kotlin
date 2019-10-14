package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author lukas
 * @since 01.10.19
 */
data class Metaproduct(val idMetaproduct: Int,
                       val localization: List<Localization>,
                       @JsonProperty("product")
                       val products: List<Product> = emptyList()) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idMetaproduct == (other as Metaproduct).idMetaproduct)

    override fun hashCode(): Int = idMetaproduct
}
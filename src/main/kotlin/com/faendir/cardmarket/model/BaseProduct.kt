package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 01.10.19
 */
interface BaseProduct {
    val idProduct: Int
    val idMetaproduct: Int
    val countReprints: Int
    val enName: String
    val localization: List<Localization>
    val website: String
    val image: String
    val gameName: String
    val categoryName: String
    val number: String
    val rarity: String
}
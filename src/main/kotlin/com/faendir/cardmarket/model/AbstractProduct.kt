package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 01.10.19
 */
abstract class AbstractProduct {
    abstract val idProduct: Int
    abstract val idMetaproduct: Int
    abstract val countReprints: Int
    abstract val enName: String
    abstract val localization: List<Localization>
    abstract val website: String
    abstract val image: String
    abstract val gameName: String
    abstract val categoryName: String
    abstract val number: String
    abstract val rarity: String
}
package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 01.10.19
 */
data class Article(val idArticle: Int,
                   override val idProduct: Int,
                   val language: Language,
                   val comments: String,
                   val price: Double,
                   val count: Int,
                   val inShoppingCart: Boolean,
                   val seller: User?,
                   val condition: Condition,
                   val isFoil: Boolean,
                   val isSigned: Boolean,
                   val isAltered: Boolean,
                   val isPlayset: Boolean)  : HasProductId {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idArticle == (other as Article).idArticle)

    override fun hashCode(): Int = idArticle
}


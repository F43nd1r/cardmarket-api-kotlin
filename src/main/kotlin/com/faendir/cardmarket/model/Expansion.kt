package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 01.10.19
 */
data class Expansion(val idExpansion: Int,
                     val enName: String,
                     val localization: List<Localization>,
                     val abbreviation: String,
                     val icon: Int,
                     val releaseDate: String,
                     val isReleased: Boolean,
                     val idGame: Int) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idExpansion == (other as Expansion).idExpansion)

    override fun hashCode(): Int = idExpansion
}


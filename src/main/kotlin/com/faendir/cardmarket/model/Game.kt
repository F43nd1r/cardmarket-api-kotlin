package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 01.10.19
 */
data class Game(val idGame: Int, val name: String, val abbreviation: String) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idGame == (other as Game).idGame)

    override fun hashCode(): Int = idGame
}
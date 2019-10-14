package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 02.10.19
 */
data class Message(val idMessage : String, val isSending: Boolean, val date : String, val text : String, val unread : Boolean) {
    override fun equals(other: Any?): Boolean = this === other || (javaClass == other?.javaClass && idMessage == (other as Message).idMessage)

    override fun hashCode(): Int = idMessage.hashCode()
}
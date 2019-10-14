@file:Suppress("unused")

package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Account
import com.faendir.cardmarket.model.DisplayLanguage
import com.faendir.cardmarket.model.Message
import com.faendir.cardmarket.model.MessageThread
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * @author lukas
 * @since 02.10.19
 */
class AccountService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getAccount(): Account? = get("account", "account")

    fun setOnVacation(onVacation: Boolean, cancelOrders: Boolean? = null, relistItems: Boolean? = null): Account? =
            put("account/vacation?" + mapOf(
                    "onVacation" to onVacation,
                    "cancelOrders" to cancelOrders,
                    "relistItems" to relistItems
            ).entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "account")

    fun setDisplayLanguage(displayLanguage: DisplayLanguage): Account? = put("account/language?idDisplayLanguage=$displayLanguage", "account")

    fun getMessageThreads(): List<MessageThread> = get("account/messages", "thread") ?: emptyList()

    fun getMessageThread(idOtherUser: Int): MessageThread? = get("account/messages/$idOtherUser", null)

    fun getMessage(idOtherUser: Int, idMessage: String): MessageThread? = get("account/messages/$idOtherUser/$idMessage", "message")

    fun sendMessage(idOtherUser: Int, message: String): Unit = post("account/messages/$idOtherUser", "message" to message)

    fun deleteMessageThread(idOtherUser: Int) = delete("account/messages/$idOtherUser")

    fun deleteMessage(idOtherUser: Int, idMessage: String) = delete("account/messages/$idOtherUser/$idMessage")

    fun findMessages(unread: Boolean? = null, startDate: OffsetDateTime? = null, endDate: OffsetDateTime? = null): List<Message> =
            get("account/messages/find?" + mapOf("unread" to unread,
                    "startDate" to startDate?.withNano(0)?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    "endDate" to endDate?.withNano(0)?.format(DateTimeFormatter.ISO_DATE_TIME))
                    .entries.filter { it.value != null }.joinToString(separator = "&") { "${it.key}=${it.value}" }, "message") ?: emptyList()

}
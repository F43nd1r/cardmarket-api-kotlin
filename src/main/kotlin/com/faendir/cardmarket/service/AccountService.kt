@file:Suppress("unused")

package com.faendir.cardmarket.service

import com.faendir.cardmarket.config.CardmarketApiConfiguration
import com.faendir.cardmarket.model.Account
import com.faendir.cardmarket.model.DisplayLanguage
import com.faendir.cardmarket.model.Message
import com.faendir.cardmarket.model.MessageThread
import com.faendir.cardmarket.util.leafs
import java.time.OffsetDateTime

/**
 * @author lukas
 * @since 02.10.19
 */
class AccountService(config: CardmarketApiConfiguration) : AbstractService(config) {
    fun getAccount(): Account? = get("account").submit("account")

    fun setOnVacation(onVacation: Boolean, cancelOrders: Boolean? = null, relistItems: Boolean? = null): Account? =
            put("account/vacation").params(
                    "onVacation" to onVacation,
                    "cancelOrders" to cancelOrders,
                    "relistItems" to relistItems
            ).submit("account")

    fun setDisplayLanguage(displayLanguage: DisplayLanguage): Account? = put("account/language").params("idDisplayLanguage" to displayLanguage).submit("account")

    fun getMessageThreads(): List<MessageThread> = get("account/messages").submit("thread") ?: emptyList()

    fun getMessageThread(idOtherUser: Int): MessageThread? = get("account/messages/$idOtherUser").submit()

    fun getMessage(idOtherUser: Int, idMessage: String): MessageThread? = get("account/messages/$idOtherUser/$idMessage").submit()

    fun sendMessage(idOtherUser: Int, message: String): Unit = post("account/messages/$idOtherUser").body(leafs("message", message)).submit() ?: Unit

    fun deleteMessageThread(idOtherUser: Int) = delete("account/messages/$idOtherUser").submit() ?: Unit

    fun deleteMessage(idOtherUser: Int, idMessage: String) = delete("account/messages/$idOtherUser/$idMessage").submit() ?: Unit

    fun findMessages(unread: Boolean? = null, startDate: OffsetDateTime? = null, endDate: OffsetDateTime? = null): List<Message> =
            get("account/messages/find").params(
                    "unread" to unread,
                    "startDate" to startDate?.format(formatter),
                    "endDate" to endDate?.format(formatter)
            ).submit("message") ?: emptyList()
}
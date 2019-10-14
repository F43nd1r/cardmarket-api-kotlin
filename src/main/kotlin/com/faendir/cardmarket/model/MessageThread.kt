package com.faendir.cardmarket.model

/**
 * @author lukas
 * @since 02.10.19
 */
data class MessageThread(val partner : User, val message : List<Message>, val unreadMessages : Int)
package com.faendir.cardmarket.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author lukas
 * @since 02.10.19
 */
data class MessageThread(val partner : User,
                         @JsonProperty("message")
                         val messages : List<Message>,
                         val unreadMessages : Int)
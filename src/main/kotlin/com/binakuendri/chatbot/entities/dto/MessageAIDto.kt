package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.chat.Message
import java.time.Instant
import java.time.format.DateTimeFormatter

data class MessageAIDto(
    val id: Long = 0,
    var sender: String? = null,
    var createdAt: String? = null,
    var content: String = "",
    var sentAt: String? = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

)

package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.chat.Message
import java.time.Instant
import java.time.format.DateTimeFormatter

data class MessageDto(
    val id: Long = 0,
    var chat: ChatDto? = null,
    var sender: UserDto? = null,
    var content: String = "",
    var sentAt: String? = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

)
fun MessageDto.toEntity(): Message {
    return Message(
        id = this.id,
        chat = this.chat?.toEntity(),
        sender = this.sender?.toEntity(),
        content = this.content,
        sentAt = Instant.parse(this.sentAt)
    )
}

fun Message.toDto(): MessageDto {
    return MessageDto(
        id = this.id,
        chat = this.chat?.toDto(),
        sender = this.sender?.toDto(),
        content = this.content,
        sentAt = DateTimeFormatter.ISO_INSTANT.format(this.sentAt)
    )
}

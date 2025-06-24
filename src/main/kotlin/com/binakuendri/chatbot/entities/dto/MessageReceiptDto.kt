package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.chat.ChatMember
import com.binakuendri.chatbot.entities.chat.MessageReceipt
import jakarta.persistence.*
import java.time.LocalDateTime


data class MessageReceiptDto(

    val id: Long = 0,
    val message: MessageDto? = null,
    val user: UserDto? = null,
    var readAt: LocalDateTime? = null
)

fun MessageReceiptDto.toEntity(): MessageReceipt {
    return MessageReceipt(
        message = this.message?.toEntity(),
        user = this.user?.toEntity(),
        readAt = this.readAt
    )
}

fun MessageReceipt.toDto(): MessageReceiptDto {
    return MessageReceiptDto(
        id = this.id,
        message = this.message?.toDto(),
        user = this.user?.toDto(),
        readAt = this.readAt
    )
}
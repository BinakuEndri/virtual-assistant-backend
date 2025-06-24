package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.chat.Chat

data class ChatDto(
    val id: Long = 0,
    val chatName: String? = null,
    val isGroup: Boolean = false,
    var lastMessage : MessageDto? = null,
)

fun ChatDto.toEntity(): Chat {
    return Chat(
        id = this.id,
        chatName = this.chatName,
        isGroup = this.isGroup,
    )
}

fun Chat.toDto(): ChatDto {
    return ChatDto(
        id = this.id,
        chatName = this.chatName,
        isGroup = this.isGroup,
    )
}

package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.chat.ChatMember
import java.time.LocalDateTime

data class ChatMemberDto(
    val id: Long=0,
    val chat: ChatDto? = null,
    val user: UserDto? = null,
    val joinedAt: LocalDateTime? = null
)


fun ChatMemberDto.toEntity(): ChatMember {
    return ChatMember(
        id = this.id,
        chat = this.chat?.toEntity(),
        user = this.user?.toEntity(),
        joinedAt = this.joinedAt
    )
}

fun ChatMember.toDto(): ChatMemberDto {
    return ChatMemberDto(
        id = this.id,
        chat = this.chat?.toDto(),
        user = this.user?.toDto(),
        joinedAt = this.joinedAt
    )
}
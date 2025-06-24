package com.binakuendri.chatbot.entities.request

import com.binakuendri.chatbot.entities.dto.ChatDto

data class CreateChatRequest(
    val chatName: String? = "",
    val isGroup: Boolean = false,
    val members : List<Int>
)

fun CreateChatRequest.getChat():ChatDto{
    return ChatDto(
        chatName = chatName,
        isGroup = isGroup
    )
}

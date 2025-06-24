package com.binakuendri.chatbot.entities.responses


import com.binakuendri.chatbot.entities.dto.ChatDto
import com.binakuendri.chatbot.entities.dto.UserDto


data class ChatMembersResponse(

    val chat: ChatDto? = null,

    val members: List<UserDto?>? = null,

)
package com.binakuendri.chatbot.entities.responses

import com.binakuendri.chatbot.entities.dto.ChatDto
import com.binakuendri.chatbot.entities.dto.UserDto

data class GetChatsResponse (
    val chat : ChatDto,
    val participants: List<UserDto>,
    val unReadCount : Int
)
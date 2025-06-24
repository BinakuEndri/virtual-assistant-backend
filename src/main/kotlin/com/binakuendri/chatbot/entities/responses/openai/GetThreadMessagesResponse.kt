package com.binakuendri.chatbot.entities.responses.openai

import com.binakuendri.chatbot.entities.dto.MessageAIDto
import com.binakuendri.chatbot.entities.dto.MessageDto

data class GetThreadMessagesResponse(
    val data: List<ThreadMessageResponse>?= emptyList(),
    val first_id: String?=null,
    val last_id: String?=null,
    val has_more: Boolean?=null,
){

    val messages : List<MessageAIDto> get() {
        val messages = mutableListOf<MessageAIDto>()
        for (message in data!!) {
             messages.add(
                 MessageAIDto(
                     sender = message.role,
                     content = message.content.get(0).text.value,
                     createdAt = message.created_at.toString()
                 )
             )
        }

        return messages
    }
}
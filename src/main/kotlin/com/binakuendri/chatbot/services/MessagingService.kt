package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.dto.MessageDto
import com.binakuendri.chatbot.entities.request.SendMessageRequest
import com.google.gson.Gson
import jakarta.validation.Valid
import org.json.JSONObject


interface  MessagingService{

    fun sendMessage(@Valid request: SendMessageRequest): MessageDto

    fun getChatMessages(chatId: Long): List<MessageDto>
}


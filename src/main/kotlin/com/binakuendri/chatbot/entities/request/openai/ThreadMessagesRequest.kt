package com.binakuendri.chatbot.entities.request.openai

data class ThreadMessagesRequest(
    val role: String,
    val content: String
)
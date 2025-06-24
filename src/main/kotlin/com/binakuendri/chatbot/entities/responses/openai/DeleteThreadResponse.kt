package com.binakuendri.chatbot.entities.responses.openai

data class DeleteThreadResponse(
    val id: String,
    val `object`: String,
    val deleted: Boolean
)
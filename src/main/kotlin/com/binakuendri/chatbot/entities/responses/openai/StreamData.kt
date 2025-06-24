package com.binakuendri.chatbot.entities.responses.openai

data class StreamData(
    val runId : String?,
    val status: String,
    val content: String?,
    val obj : String?
)
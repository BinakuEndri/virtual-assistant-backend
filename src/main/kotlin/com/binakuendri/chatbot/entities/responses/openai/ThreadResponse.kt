package com.binakuendri.chatbot.entities.responses.openai

data class ThreadResponse(
    val id: String,
    val `object`: String,
    val created_at: Long,
    val metadata: Map<String, Any>,
    val tool_resources: Map<String, Any>
)
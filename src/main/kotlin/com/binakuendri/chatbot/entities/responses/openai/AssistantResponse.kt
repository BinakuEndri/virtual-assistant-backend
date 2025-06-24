package com.binakuendri.chatbot.entities.responses.openai

data class AssistantResponse(
    val id: String,
    val `object`: String,
    val created_at: Long,
    val name: String,
    val description: String?,
    val model: String,
    val instructions: String,
    val tools: List<Tool>,
    val metadata: Map<String, Any>,
    val top_p: Double,
    val temperature: Double,
    val response_format: String
)
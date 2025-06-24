package com.binakuendri.chatbot.entities.responses.openai

data class ThreadMessageResponse(
    val id: String,
    val `object`: String,
    val created_at: Long,
    val assistant_id: String?,
    val thread_id: String,
    val run_id: String?,
    val role: String,
    val content: List<MessageContent>,
    val attachments: List<Any>,
    val metadata: Map<String, Any>
)

data class MessageContent(
    val type: String,
    val text: MessageText
)

data class MessageText(
    val value: String,
    val annotations: List<Any>
)
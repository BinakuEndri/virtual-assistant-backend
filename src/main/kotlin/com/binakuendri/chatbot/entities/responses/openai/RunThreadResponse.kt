package com.binakuendri.chatbot.entities.responses.openai

data class RunThreadResponse(
    val id: String,
    val `object`: String,
    val created_at: Long,
    val assistant_id: String,
    val thread_id: String,
    val status: String,
    val started_at: Long?,
    val expires_at: Long?,
    val cancelled_at: Long?,
    val failed_at: Long?,
    val completed_at: Long?,
    val last_error: Any?,
    val model: String,
    val instructions: String?,
    val incomplete_details: Any?,
    val tools: List<Tool>,
    val metadata: Map<String, Any>,
    val usage: Usage?,
    val temperature: Double,
    val top_p: Double,
    val max_prompt_tokens: Int,
    val max_completion_tokens: Int,
    val truncation_strategy: Map<String, Any>,
    val response_format: String,
    val tool_choice: String,
    val parallel_tool_calls: Boolean
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

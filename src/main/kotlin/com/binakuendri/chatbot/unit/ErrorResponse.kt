package com.binakuendri.chatbot.unit

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Long = System.currentTimeMillis()
)
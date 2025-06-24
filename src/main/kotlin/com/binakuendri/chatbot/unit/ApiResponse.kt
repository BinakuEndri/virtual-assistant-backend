package com.binakuendri.chatbot.unit

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)
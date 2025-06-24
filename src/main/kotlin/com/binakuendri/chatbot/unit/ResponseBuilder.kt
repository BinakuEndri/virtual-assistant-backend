package com.binakuendri.chatbot.unit

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ResponseBuilder {
    fun <T> build(status: HttpStatus, message: String, data: T? = null): ResponseEntity<ApiResponse<T>> {
        val response = ApiResponse(status.value(), message, data)
        return ResponseEntity(response, status)
    }
}
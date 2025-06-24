package com.binakuendri.chatbot.entities.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class SendMessageRequest(
    @field:NotNull @field:Min(1) val chatId: Long,
    @field:NotBlank val message: String
)

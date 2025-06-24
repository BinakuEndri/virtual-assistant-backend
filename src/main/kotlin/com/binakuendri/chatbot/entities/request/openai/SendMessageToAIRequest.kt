package com.binakuendri.chatbot.entities.request.openai

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SendMessageToAIRequest(
    @field:NotNull @field:Min(1) val threadType: String,
    @field:NotBlank val message: String
)
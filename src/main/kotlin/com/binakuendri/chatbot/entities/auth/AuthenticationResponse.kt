package com.binakuendri.chatbot.entities.auth

import com.binakuendri.chatbot.entities.dto.UserDto
import com.fasterxml.jackson.annotation.JsonProperty

class AuthenticationResponse(
    @field:JsonProperty("access_token") val accessToken: String? = "",
    @field:JsonProperty("user") val user: UserDto? = null,
    @field:JsonProperty("refresh_token") val refreshToken: String?="",
    @field:JsonProperty("message") val message: String?=""
)
package com.binakuendri.chatbot.controllers.api.v1

import com.binakuendri.chatbot.entities.auth.AuthenticationResponse
import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.request.LoginRequest
import com.binakuendri.chatbot.services.auth.AuthenticationService
import com.binakuendri.chatbot.unit.ApiResponse
import com.binakuendri.chatbot.unit.ResponseBuilder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationControllerV1(@Autowired val authService: AuthenticationService) {


    @PostMapping("/register")
    fun register(
        @RequestBody request: User
    ): ResponseEntity<ApiResponse<AuthenticationResponse>> {

        return ResponseBuilder.build(
            status = HttpStatus.CREATED,
            message = "",
            data = authService.register(request)
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthenticationResponse>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = authService.authenticate(request)
        )
    }

    @PostMapping("/refresh_token")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<Any>> {
        val apiResponse = authService.refreshToken(request, response)
        return ResponseBuilder.build(
            status = apiResponse.statusCode as HttpStatus,
            message = "",
            data = apiResponse.body
        )
    }
}
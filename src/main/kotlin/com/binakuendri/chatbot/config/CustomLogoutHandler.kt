package com.binakuendri.chatbot.config

import com.binakuendri.chatbot.entities.auth.Token
import com.binakuendri.chatbot.repository.TokenRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler

@Configuration
class CustomLogoutHandler(private val tokenRepository: TokenRepository) : LogoutHandler {
    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        val token = authHeader.substring(7)
        val storedToken: Token = tokenRepository.findByAccessToken(token).orElse(null)

        storedToken?.let {
            storedToken.isLoggedOut = true
            tokenRepository.save<Token>(storedToken)
        }
    }
}
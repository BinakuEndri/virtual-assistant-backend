package com.binakuendri.chatbot.services.auth

import com.binakuendri.chatbot.entities.auth.Token
import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.UserDto
import com.binakuendri.chatbot.entities.dto.toDto
import com.binakuendri.chatbot.repository.TokenRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService(private val tokenRepository: TokenRepository) {
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${application.security.jwt.access-token-expiration}")
    private val accessTokenExpire: Long = 0

    @Value("\${application.security.jwt.refresh-token-expiration}")
    private val refreshTokenExpire: Long = 0


    fun extractUsername(token: String?): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }


    fun isValid(token: String?, user: UserDetails): Boolean {
        val username = extractUsername(token)

        val validToken = tokenRepository
            .findByAccessToken(token!!)
            .map { t: Token -> !t.isLoggedOut }
            .orElse(false)

        return (username == user.username) && !isTokenExpired(token) && validToken
    }

    fun isValidRefreshToken(token: String?, user: User): Boolean {
        val username = extractUsername(token)

        val validRefreshToken = tokenRepository
            .findByRefreshToken(token!!)
            .map { t: Token -> !t.isLoggedOut }
            .orElse(false)

        return (username == user.username) && !isTokenExpired(token) && validRefreshToken
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    fun <T> extractClaim(token: String?, resolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return resolver.apply(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parser()
            .verifyWith(signinKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }


    fun generateAccessToken(user: User): String {
        return generateToken(user, accessTokenExpire)
    }

    fun generateRefreshToken(user: User): String {
        return generateToken(user, refreshTokenExpire)
    }

    private fun generateToken(user: User, expireTime: Long): String {
        val token: String = Jwts
            .builder()
            .subject(user.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expireTime))
            .signWith(signinKey)
            .compact()

        return token
    }

    private val signinKey: SecretKey
        get() {
            val keyBytes: ByteArray = Decoders.BASE64URL.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    fun getUserFromToken(accessToken: String): User {
        // Find the token by access token
        val token = tokenRepository.findByAccessToken(accessToken)
            .orElseThrow { NoSuchElementException("Token not found") }

        // Check if the token is valid and not logged out
        if (token.isLoggedOut) {
            throw IllegalStateException("Token has been logged out")
        }

        // Retrieve the user associated with the token
        return token.user ?: throw NoSuchElementException("User associated with token not found")
    }

    fun getUserByToken(): UserDto{
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val user =  authentication.principal as User
        return  user.toDto()
    }
}
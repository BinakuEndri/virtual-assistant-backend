package com.binakuendri.chatbot.services.auth


import com.binakuendri.chatbot.entities.auth.AuthenticationResponse
import com.binakuendri.chatbot.entities.auth.Token
import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.toDto
import com.binakuendri.chatbot.entities.request.LoginRequest
import com.binakuendri.chatbot.repository.TokenRepository
import com.binakuendri.chatbot.repository.UserRepository
import com.binakuendri.chatbot.services.OpenAiService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.function.Consumer
import javax.naming.AuthenticationException

@Service
class AuthenticationService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val tokenRepository: TokenRepository,
    private val authenticationManager: AuthenticationManager,
    private val openAiService: OpenAiService
) {

    fun register(request: User): AuthenticationResponse {
        // check if user already exist. if exist than authenticate the user

        if (repository.findByUsername(request.username)?.isPresent == true) {
            return AuthenticationResponse(null, null, "User already exist")
        }

        var user: User = User()
        user.firstName = request.firstName
        user.lastName = request.lastName
        user.setUsername( request.username)
        user.setPassword(passwordEncoder.encode(request.password))

        user.setRole(request.getRole())

        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken: String = jwtService.generateRefreshToken(user)

        val scienceThreadId =  openAiService.createThread().block()?.id
        val medicalThreadId =  openAiService.createThread().block()?.id
        user.scienceThreadId = scienceThreadId
        user.medicalThreadId = medicalThreadId

        user = repository.save(user)
        saveUserToken(accessToken, refreshToken, user)

        return AuthenticationResponse(accessToken,user.toDto() ,refreshToken, "User registration was successful")
    }

    fun authenticate(request: LoginRequest): AuthenticationResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password
                )
            )
        } catch (ex: AuthenticationException) {
            return AuthenticationResponse(null, null, "User login was unsuccessful: ${ex.message}")
        }

        val user: User = repository.findByUsername(request.username)
            .orElseThrow { UsernameNotFoundException("User not found") }

        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken: String = jwtService.generateRefreshToken(user)

        revokeAllTokenByUser(user)
        saveUserToken(accessToken, refreshToken, user)

        return AuthenticationResponse(accessToken,user.toDto(),refreshToken, "User login was successful")
    }


    private fun revokeAllTokenByUser(user: User) {
        user.id?.let {
            val validTokens: List<Token> = tokenRepository.findAllAccessTokensByUser(it)
            if (validTokens.isEmpty()) {
                return
            }

            validTokens.forEach(Consumer<Token> { t: Token ->
                t.isLoggedOut = true
            })

            tokenRepository.saveAll(validTokens)
        }
    }

    private fun saveUserToken(accessToken: String, refreshToken: String, user: User) {
        val token = Token()
        token.accessToken = accessToken
        token.refreshToken = refreshToken
        token.isLoggedOut = false
        token.user = user
        tokenRepository.save<Token>(token)
    }

    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse?
    ): ResponseEntity<*> {
        // extract the token from authorization header
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity<Any?>(HttpStatus.UNAUTHORIZED)
        }

        val token = authHeader.substring(7)

        // extract username from token
        val username: String = jwtService.extractUsername(token)

        // check if the user exist in database
        val user: User? = repository.findByUsername(username)
            ?.orElseThrow { RuntimeException("No user found") }

        user?.let {
            // check if the token is valid
            if (jwtService.isValidRefreshToken(token, user)) {
                // generate access token
                val accessToken: String = jwtService.generateAccessToken(user)
                val refreshToken: String = jwtService.generateRefreshToken(user)

                revokeAllTokenByUser(user)
                saveUserToken(accessToken, refreshToken, user)

                return ResponseEntity<Any?>(
                    AuthenticationResponse(accessToken,user.toDto(), refreshToken, "New token generated"),
                    HttpStatus.OK
                )
            }
        }

        return ResponseEntity<Any?>(HttpStatus.UNAUTHORIZED)
    }

}
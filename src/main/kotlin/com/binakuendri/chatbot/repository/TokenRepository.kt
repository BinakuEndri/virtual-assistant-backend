package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.auth.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface TokenRepository : JpaRepository<Token, Int> {

    @Query("""
        select t from Token t join t.user u 
        where u.id = :userId and t.isLoggedOut = false
    """)
    fun findAllAccessTokensByUser(userId: Int): List<Token>

    fun findByAccessToken(token: String): Optional<Token>

    fun findByRefreshToken(token: String): Optional<Token>
}
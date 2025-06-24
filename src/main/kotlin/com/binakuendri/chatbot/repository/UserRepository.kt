package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.UserDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Int?> {
    fun findByUsername(username: String?): Optional<User>
    fun findById(userid: Int?): Optional<User>
}

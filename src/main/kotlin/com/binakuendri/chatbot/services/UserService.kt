package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.UserDto
import org.springframework.stereotype.Service


@Service
interface UserService {

    fun getUserByUsername(username: String): UserDto

    fun createUser(user: User): UserDto

    fun findById(id: Int): UserDto

    fun findUsersById(usersId: List<Int>): List<UserDto>

    fun getAllUsers(): List<UserDto>

}
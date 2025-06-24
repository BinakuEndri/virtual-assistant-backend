package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.UserDto
import com.binakuendri.chatbot.entities.dto.toDto
import com.binakuendri.chatbot.repository.UserRepository
import com.binakuendri.chatbot.services.ChatService
import com.binakuendri.chatbot.services.UserService
import com.binakuendri.chatbot.services.auth.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.NoSuchElementException


@Service
@Qualifier("v1")

class UserServiceV1Impl(
    @Autowired private val userRepository: UserRepository,
) : UserService {

    override fun getUserByUsername(
        username: String
    ): UserDto {
        return userRepository
            .findByUsername(username = username)
            .orElse(null)
            .toDto()
    }

    override fun createUser(
        user: User
    ): UserDto {
        return userRepository.save(user).toDto()
    }

    override fun findById(
        id: Int
    ): UserDto {
        return userRepository.findById(userid = id)
            .orElseThrow { NoSuchElementException() }
            .toDto()
    }

    override fun findUsersById(
        usersId: List<Int>
    ): List<UserDto> {
        return usersId
            .map { findById(id = it) }
    }

    override fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().map { it.toDto() }
    }

}
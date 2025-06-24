package com.binakuendri.chatbot.entities.dto

import com.binakuendri.chatbot.entities.auth.Role
import com.binakuendri.chatbot.entities.auth.User


data class UserDto(
    var id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var role: Role? = null,
    var medicalThreadId : String? = null,
    var scienceThreadId : String? = null,
)

fun UserDto.toEntity(): User {
    return User().apply {
        id = this@toEntity.id
        firstName = this@toEntity.firstName
        lastName = this@toEntity.lastName
        setUsername( this@toEntity.username)
        role = this@toEntity.role
        medicalThreadId = this@toEntity.medicalThreadId
        scienceThreadId = this@toEntity.scienceThreadId

    }
}

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.username,
        role = this.getRole(),
        medicalThreadId = this.medicalThreadId,
        scienceThreadId = this.scienceThreadId,
    )
}
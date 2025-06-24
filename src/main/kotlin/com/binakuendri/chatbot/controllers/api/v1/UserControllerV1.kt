package com.binakuendri.chatbot.controllers.api.v1

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.dto.UserDto
import com.binakuendri.chatbot.services.UserService
import com.binakuendri.chatbot.unit.ApiResponse
import com.binakuendri.chatbot.unit.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserControllerV1(@Autowired @Qualifier("v1") private val userService: UserService) {

    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<ApiResponse<UserDto>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = userService.getUserByUsername(username)
        )
    }

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<ApiResponse<UserDto>> {
        return ResponseBuilder.build(
            status = HttpStatus.CREATED,
            message = "",
            data = userService.createUser(user)
        )
    }
}
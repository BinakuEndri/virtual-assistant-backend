package com.binakuendri.chatbot.controllers.api.v1

import com.binakuendri.chatbot.entities.dto.ChatDto
import com.binakuendri.chatbot.entities.dto.MessageReceiptDto
import com.binakuendri.chatbot.entities.dto.UserDto
import com.binakuendri.chatbot.entities.request.CreateChatRequest
import com.binakuendri.chatbot.entities.responses.ChatMembersResponse
import com.binakuendri.chatbot.entities.responses.GetChatsResponse
import com.binakuendri.chatbot.entities.responses.UnReadCountResponse
import com.binakuendri.chatbot.services.ChatService
import com.binakuendri.chatbot.unit.ApiResponse
import com.binakuendri.chatbot.unit.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chat")
class ChatControllerV1(
    @Autowired @Qualifier("v1") private val chatService: ChatService
) {

    @PostMapping
    fun create(
        @RequestBody request: CreateChatRequest
    ): ResponseEntity<ApiResponse<ChatDto>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.createChat(request)
        )
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: ChatDto
    ): ResponseEntity<ApiResponse<ChatDto>> {

        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.updateChat(id, request)
        )
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.deleteChat(id)
        )
    }

    @GetMapping("/{chatId}/members")
    fun getChatMembers(
        @PathVariable chatId: Long
    ): ResponseEntity<ApiResponse<ChatMembersResponse>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.getChatMembers(chatId)
        )
    }

    @GetMapping()
    fun getUserChats(
    ): ResponseEntity<ApiResponse<List<GetChatsResponse>>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.getUserChats()
        )
    }

    @PostMapping("/{chatId}/read")
    fun readChat(
        @PathVariable chatId: Long
    ): ResponseEntity<ApiResponse<List<MessageReceiptDto>>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.readAllMessagesOfChat(chatId)
        )
    }

    @GetMapping("/unReadCount")
    fun getUnReadCount():ResponseEntity<ApiResponse<UnReadCountResponse>>{
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.getUnReadCount()
        )
    }

    @DeleteMapping("/{chatId}/members/{memberId}")
    fun removeMember(
        @PathVariable chatId: Long,
        @PathVariable memberId: Int
    ):ResponseEntity<ApiResponse<Unit>>{
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.removeMember(chatId,memberId)
        )
    }

    @GetMapping("/usersToChat")
    fun getUsers(): ResponseEntity<ApiResponse<List<UserDto>>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = chatService.getUsersToChatWith()
        )
    }
}
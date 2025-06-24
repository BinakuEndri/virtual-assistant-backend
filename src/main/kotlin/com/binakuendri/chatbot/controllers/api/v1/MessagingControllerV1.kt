package com.binakuendri.chatbot.controllers.api.v1

import com.binakuendri.chatbot.entities.dto.MessageDto
import com.binakuendri.chatbot.entities.request.SendMessageRequest
import com.binakuendri.chatbot.services.MessagingService
import com.binakuendri.chatbot.unit.ApiResponse
import com.binakuendri.chatbot.unit.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/message")
class MessagingControllerV1(@Autowired @Qualifier("v1") private val messagingService: MessagingService) {

    @PostMapping("/send")
    fun sendMessage(
        @RequestBody request: SendMessageRequest,
    ): ResponseEntity<ApiResponse<MessageDto>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = messagingService.sendMessage(request),
        )
    }

    @GetMapping("/chat/{chatId}")
    fun getMessages(
        @PathVariable chatId: Long,
        ): ResponseEntity<ApiResponse<List<MessageDto>>> {
        return ResponseBuilder.build(
            status = HttpStatus.OK,
            message = "",
            data = messagingService.getChatMessages(chatId)
        )
    }
}
package com.binakuendri.chatbot.controllers.api.v1

import com.binakuendri.chatbot.entities.dto.MessageAIDto
import com.binakuendri.chatbot.entities.dto.MessageDto
import com.binakuendri.chatbot.entities.request.openai.SendMessageToAIRequest
import com.binakuendri.chatbot.entities.request.openai.ThreadMessagesRequest
import com.binakuendri.chatbot.entities.responses.openai.AssistantResponse
import com.binakuendri.chatbot.entities.responses.openai.GetThreadMessagesResponse
import com.binakuendri.chatbot.entities.responses.openai.StreamData
import com.binakuendri.chatbot.entities.responses.openai.ThreadMessageResponse
import com.binakuendri.chatbot.services.OpenAiService
import com.binakuendri.chatbot.unit.ApiResponse
import com.binakuendri.chatbot.unit.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/openai")

class OpenAIController(@Autowired private val openAiService: OpenAiService) {

    @PostMapping("/createAssistant")
    fun createAssistant(): ResponseEntity<ApiResponse<AssistantResponse>> {
        return try {
            val response = openAiService.createAssistant().block()

            ResponseBuilder.build(
                status = HttpStatus.OK,
                message = "Success",
                data = response
            )
        } catch (e: Exception) {
            ResponseBuilder.build(
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "Failed to get response from OpenAI: ${e.message}",
                data = null
            )
        }
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody request: SendMessageToAIRequest): ResponseEntity<ApiResponse<ThreadMessageResponse>> {
        return try {
            val response = openAiService.sendMessageToThread(
                request.threadType,
                ThreadMessagesRequest("user", request.message)
            ).block()

            ResponseBuilder.build(
                status = HttpStatus.OK,
                message = "Success",
                data = response
            )
        } catch (e: Exception) {
            ResponseBuilder.build(
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "Failed to get response from OpenAI: ${e.message}",
                data = null
            )
        }
    }

    @GetMapping("/run", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamChat(
        @RequestParam threadType: String,
    ): Flux<ApiResponse<StreamData>> {
        return try {
            openAiService.runThread(threadType)
                .map { streamData ->
                    ApiResponse(
                        status = HttpStatus.OK.value(),
                        message = "Success",
                        data = streamData
                    )
                }
        } catch (e: Exception) {
            Flux.just(
                ApiResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = "Failed to get response from OpenAI: ${e.message}",
                    data = null
                )
            )
        }
    }

    @GetMapping("/getThreadMessages")
    fun getThreadMessages(
        @RequestParam threadType: String,
    ): ResponseEntity<ApiResponse<List<MessageAIDto>>> {
        return try {
            val response = openAiService.getThreadMessages(threadType).block()?.messages

            ResponseBuilder.build(
                status = HttpStatus.OK,
                message = "Success",
                data = response
            )
        } catch (e: Exception) {
            ResponseBuilder.build(
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = "Failed to get response from OpenAI: ${e.message}",
                data = null
            )
        }
    }
}
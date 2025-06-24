package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.request.openai.ThreadMessagesRequest
import com.binakuendri.chatbot.entities.responses.openai.*
import com.binakuendri.chatbot.services.auth.JwtService
import com.binakuendri.chatbot.unit.parseEvent
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OpenAiService(
    private val webClient: WebClient.Builder,
    @Autowired private val jwtService: JwtService,
) {

    private val dotenv = Dotenv.load()
    private val apiKey: String = dotenv["OPENAI_API_KEY"]
        ?: throw IllegalStateException("Missing OpenAI API Key")

    private val openAiUrl = "https://api.openai.com/v1"

    fun createAssistant(): Mono<AssistantResponse> {
        return webClient.build()
            .post()
            .uri("$openAiUrl/assistants")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(AssistantResponse::class.java)
    }

    fun getAssistant(assistantId: String): Mono<AssistantResponse> {
        return webClient.build()
            .get()
            .uri("$openAiUrl/assistants/$assistantId")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(AssistantResponse::class.java)
    }

    fun createThread(): Mono<ThreadResponse> {
        return webClient.build()
            .post()
            .uri("$openAiUrl/threads")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{}") // Empty JSON body
            .retrieve()
            .bodyToMono(ThreadResponse::class.java)
    }

    fun getThread(threadId: String): Mono<ThreadResponse> {
        val url = "$openAiUrl/threads/$threadId"

        return webClient.build()
            .get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ThreadResponse::class.java)
    }

    fun deleteThread(threadId: String): Mono<DeleteThreadResponse> {
        val url = "$openAiUrl/threads/$threadId"

        return webClient.build()
            .delete()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(DeleteThreadResponse::class.java)
    }

    fun sendMessageToThread(threadType: String, request: ThreadMessagesRequest): Mono<ThreadMessageResponse> {

        val user = jwtService.getUserByToken()
        val threadId = when(threadType) {
            "medical" -> user.medicalThreadId
            "science" -> user.scienceThreadId
            else -> {user.medicalThreadId}
        }
        val url = "$openAiUrl/threads/$threadId/messages"
        return webClient.build()
            .post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ThreadMessageResponse::class.java)
    }

    fun getThreadMessages(threadType: String): Mono<GetThreadMessagesResponse> {

        val user = jwtService.getUserByToken()
        val threadId = when(threadType) {
            "medical" -> user.medicalThreadId
            "science" -> user.scienceThreadId
            else -> {user.medicalThreadId}
        }

        val url = "$openAiUrl/threads/$threadId/messages"

        return webClient.build()
            .get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(GetThreadMessagesResponse::class.java)
    }

    fun runThread(threadType: String): Flux<StreamData> {

        val user = jwtService.getUserByToken()
        var threadId :String?=null
        var assistantId:String?=null
        when(threadType) {
            "medical" -> {
                threadId = user.medicalThreadId
                assistantId = "asst_FMEHk2dNSmJjTtnBwDz4wzln"
            }
            "science" -> {
                threadId = user.scienceThreadId
                assistantId = "asst_FMEHk2dNSmJjTtnBwDz4wzln"
            }
            else -> {
                threadId = user.scienceThreadId
                assistantId = "asst_FMEHk2dNSmJjTtnBwDz4wzln"
            }
        }

        val requestBody = mapOf(
            "assistant_id" to assistantId,
            "stream" to true
        )

        return webClient.build()
            .post()
            .uri("https://api.openai.com/v1/threads/$threadId/runs")
            .header("Authorization", "Bearer $apiKey")
            .header("OpenAI-Beta", "assistants=v2")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToFlux(String::class.java)
            .map { parseEvent(it) }
    }
}
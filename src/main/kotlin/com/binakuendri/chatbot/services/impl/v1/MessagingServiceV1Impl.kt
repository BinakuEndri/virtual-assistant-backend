package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.dto.MessageDto
import com.binakuendri.chatbot.entities.request.SendMessageRequest
import com.binakuendri.chatbot.services.*
import com.binakuendri.chatbot.services.auth.JwtService
import com.binakuendri.chatbot.services.impl.PusherService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("v1")

class MessagingServiceV1Impl(
    private val pusherService: PusherService,
    @Autowired private val messageService: MessageService,
    @Autowired private val chatService: ChatService,
    @Autowired private val jwtService: JwtService,
    private val messageReceiptService: MessageReceiptService,
): MessagingService  {

    override fun sendMessage(
        @Valid request: SendMessageRequest
    ): MessageDto{
        val chat = chatService.findById(chatId = request.chatId)
        val sender = jwtService.getUserByToken()

        val message = messageService.save(
          message =  MessageDto(
                chat = chat,
                sender = sender,
                content = request.message
            )
        )

        val members = chatService.findChatMembers(chat = chat)
            .filter { it.id != sender.id }

        messageReceiptService.createMultipleMessageReceipts(
            message = message,
            users = members
        )
        pusherService.chatEvent(
            members = members.mapNotNull { it.id },
            event = "new-message",
            data = message
        )

        return message
    }

    override fun getChatMessages(
        chatId: Long
    ): List<MessageDto>{
        return messageService.findByChat(
            chat = chatService.findById(
                chatId = chatId
            )
        )
    }

}
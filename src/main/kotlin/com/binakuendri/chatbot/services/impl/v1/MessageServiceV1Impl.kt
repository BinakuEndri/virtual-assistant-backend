package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.repository.MessageRepository
import com.binakuendri.chatbot.services.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.List

@Service
@Qualifier("v1")

class MessageServiceV1Impl(
    @Autowired private val messageRepository: MessageRepository,
) :MessageService{

    override fun findById(
        messageId: Long
    ): MessageDto {
        return messageRepository.findById(messageId = messageId)
            .orElseThrow { NoSuchElementException("Message not found with id $messageId") }.toDto()
    }

    override fun save(
        message: MessageDto
    ): MessageDto {
        return messageRepository.save(
            message.toEntity()
        ).toDto()
    }

    override fun findByChat(
        chat: ChatDto
    ): List<MessageDto> {
        return messageRepository.findByChat(chat = chat.toEntity())
            .orElseThrow { NoSuchElementException("Message not found with id ${chat.id}") }
            .map { it.toDto() }
            .sortedBy { it.sentAt }
    }

    override fun getReceivedMessages(
        chat: ChatDto,
        sender: UserDto
    ): List<MessageDto> {
        return findByChat(chat = chat).filter { it.sender != sender }
    }

    override fun getLatestMessageOfChat(
        chat: ChatDto
    ): MessageDto? {
       return findByChat(chat = chat).firstOrNull()
    }
}

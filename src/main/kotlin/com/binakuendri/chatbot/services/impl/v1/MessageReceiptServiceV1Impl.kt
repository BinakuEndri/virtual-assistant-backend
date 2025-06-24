package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.chat.MessageReceipt
import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.repository.MessageReceiptRepository
import com.binakuendri.chatbot.services.MessageReceiptService
import com.binakuendri.chatbot.services.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
@Qualifier("v1")

class MessageReceiptServiceV1Impl(
    @Autowired private val messageReceiptRepository: MessageReceiptRepository,
    private val messageService: MessageService,
): MessageReceiptService {

    override fun save(
        messageReceipt: MessageReceiptDto
    ) : MessageReceiptDto {
        return messageReceiptRepository.save(
            messageReceipt.toEntity()
        ).toDto()
    }

    override fun findById(
        id: Long?
    ): MessageReceipt {
        return messageReceiptRepository.findById(messageId = id)
            .orElseThrow { NoSuchElementException("MessageReceipt not found with id $id") }
    }

    override fun findByMessage(
        message: MessageDto
    ): List<MessageReceiptDto> {
        return messageReceiptRepository.findByMessage(message = message.toEntity())
            .orElseThrow { NoSuchElementException("MessageReceipt not found for the provided message") }
            .map { it.toDto() }
    }

    override fun findByMessageAndUser(
        message: MessageDto,
        user: UserDto
    ): MessageReceipt {
        val receipt = messageReceiptRepository.findByMessageAndUser(
            message = message.toEntity(),
            user = user.toEntity()
        ).orElseThrow {
            NoSuchElementException("MessageReceipt not found for the provided message")
        }
        return receipt
    }

    override fun findMessageByReceiver(
        user: UserDto
    ): List<MessageReceiptDto> {
        return messageReceiptRepository.findByUser(user = user.toEntity())
            .orElseThrow { NoSuchElementException("MessageReceipt not found for the provided message") }
            .map { it.toDto() }
    }


    override fun createMultipleMessageReceipts(
        message: MessageDto,
        users: List<UserDto>
    ): List<MessageReceiptDto> {
        return users.map {
            addMessageReceipt(
                message = message,
                user = it
            )
        }
    }

    override fun addMessageReceipt(
        message: MessageDto,
        user: UserDto
    ): MessageReceiptDto {
        return save(
            MessageReceiptDto(
                message = message,
                user = user,
            )
        )
    }

    override fun readMessage(
        messageReceipt: MessageReceiptDto
    ): MessageReceiptDto {
        messageReceipt.readAt = LocalDateTime.now()
        return save(messageReceipt = messageReceipt)
    }

    override fun readAllMessages(
        chatDto: ChatDto,
        user: UserDto
    ): List<MessageReceiptDto>{
        return getUnReadMessagesOfChat(
            chatDto = chatDto,
            user = user
        ).map { readMessage(messageReceipt = it) }
    }

    override fun getUnReadMessagesOfChat(
        chatDto: ChatDto,
        user: UserDto
    ): List<MessageReceiptDto> {
        return messageService.getReceivedMessages(
            chat = chatDto,
            sender =  user
        )
            .map { findByMessage(message = it) }
            .flatten()
            .filter { it.readAt == null }
    }

    override fun getUnReadMessagesCount(
        chatDto: ChatDto,
        user: UserDto
    ):Int{
        return getUnReadMessagesOfChat(
            chatDto = chatDto,
            user = user
        ).size
    }

    override fun getUnReadCount(
        chats: List<ChatDto>,
        user: UserDto
    ): Int{
        val unRead = chats
            .map { getUnReadMessagesOfChat(chatDto = it, user = user) }
            .filter { it.isNotEmpty() }

        return unRead.size
    }
}
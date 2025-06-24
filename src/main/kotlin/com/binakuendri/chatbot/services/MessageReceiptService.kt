package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.chat.MessageReceipt
import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.repository.MessageReceiptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


interface MessageReceiptService {

    fun save(messageReceipt: MessageReceiptDto) : MessageReceiptDto

    fun findById(id: Long?): MessageReceipt

    fun findByMessage(message: MessageDto): List<MessageReceiptDto>

    fun findByMessageAndUser(message: MessageDto, user: UserDto): MessageReceipt

    fun findMessageByReceiver(user: UserDto): List<MessageReceiptDto>

    fun createMultipleMessageReceipts(message: MessageDto, users: List<UserDto>): List<MessageReceiptDto>

    fun addMessageReceipt(message: MessageDto, user: UserDto): MessageReceiptDto

    fun readMessage(messageReceipt: MessageReceiptDto): MessageReceiptDto

    fun readAllMessages(chatDto: ChatDto, user: UserDto): List<MessageReceiptDto>

    fun getUnReadMessagesOfChat(chatDto: ChatDto,user: UserDto): List<MessageReceiptDto>

    fun getUnReadMessagesCount(chatDto: ChatDto,user: UserDto):Int

    fun getUnReadCount(chats: List<ChatDto>,user: UserDto): Int
}
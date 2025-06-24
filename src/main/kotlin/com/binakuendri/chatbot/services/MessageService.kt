package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.dto.*
import kotlin.collections.List

interface MessageService {

    fun findById(messageId: Long): MessageDto

    fun save(message: MessageDto): MessageDto

    fun findByChat(chat: ChatDto): List<MessageDto>

    fun getReceivedMessages(chat: ChatDto, sender: UserDto): List<MessageDto>

    fun getLatestMessageOfChat(chat: ChatDto): MessageDto?
}

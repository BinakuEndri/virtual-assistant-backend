package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.entities.request.CreateChatRequest
import com.binakuendri.chatbot.entities.responses.ChatMembersResponse
import com.binakuendri.chatbot.entities.responses.GetChatsResponse
import com.binakuendri.chatbot.entities.responses.UnReadCountResponse

interface ChatService{

    fun findById(chatId: Long): ChatDto

    fun findByChatName(chatName: String) : ChatDto

    fun save(chat: ChatDto) :ChatDto

    fun saveChat(createChatRequest: CreateChatRequest): ChatDto

    fun createChat(createChatRequest: CreateChatRequest): ChatDto

    fun updateChat(id: Long, chat: ChatDto): ChatDto

    fun deleteChat(id: Long)

    fun getChatMembers(chatId: Long): ChatMembersResponse

    fun getChats(userDto: UserDto) : List<ChatDto>

    fun getUserChats(): List<GetChatsResponse>

    fun findChatMembers(chat: ChatDto): List<UserDto>

    fun readAllMessagesOfChat(chatId: Long): List<MessageReceiptDto>

    fun getUnReadChatCount(chatId: Long): Int

    fun getUnReadCount(): UnReadCountResponse

    fun removeMember(chatId: Long,userId: Int)

    fun getUsersToChatWith(): List<UserDto>
    }
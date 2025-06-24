package com.binakuendri.chatbot.services

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.chat.Chat
import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.entities.responses.ChatMembersResponse


interface ChatMemberService {

    fun save(chatMemberDto: ChatMemberDto): ChatMemberDto

    fun delete(chatMemberDto: ChatMemberDto)

    fun findByChat(chat: ChatDto): List<ChatMemberDto>

    fun findByUser(user: UserDto): List<ChatMemberDto>

    fun findByChatAndUser(chat: ChatDto, user: UserDto): ChatMemberDto

    fun addMember(chatDto :ChatDto, userDto: UserDto): ChatMemberDto

    fun addMultipleMembers(chatDto :ChatDto, users: List<UserDto>): List<ChatMemberDto>

    fun removeMember(chatDto: ChatDto, userDto: UserDto)

    fun getChatMembers(chat: ChatDto): ChatMembersResponse

    fun getUserChats(userDto: UserDto): List<ChatDto>

    fun findChatMembers(chat: ChatDto): List<UserDto>
}
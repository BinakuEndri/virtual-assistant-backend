package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.entities.responses.ChatMembersResponse
import com.binakuendri.chatbot.repository.ChatMemberRepository
import com.binakuendri.chatbot.services.ChatMemberService
import com.binakuendri.chatbot.services.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
@Qualifier("v1")

class ChatMemberServiceV1Impl(
    @Autowired private val chatMemberRepository: ChatMemberRepository,
    private val messageService: MessageService,
): ChatMemberService {

    override fun save(
        chatMemberDto: ChatMemberDto
    ): ChatMemberDto {
        return chatMemberRepository
            .save(chatMemberDto.toEntity()).toDto()
    }
    override fun delete(
        chatMemberDto: ChatMemberDto
    ) {
        chatMemberRepository.delete(chatMemberDto.toEntity())
        chatMemberDto.id?.let {
            val bool = chatMemberRepository.findById(chatMemberDto.id).isPresent
            bool
        }
    }

    override fun findByChat(
        chat: ChatDto
    ): List<ChatMemberDto> {
        return chatMemberRepository
            .findByChat(chat = chat.toEntity())
            .orElseThrow { NoSuchElementException("No members found for chat with id ${chat.id}") }
            .map { it.toDto() }
    }

    override fun findByUser(
        user: UserDto
    ): List<ChatMemberDto> {
        return chatMemberRepository
            .findByUser(user = user.toEntity())
            .orElseThrow { NoSuchElementException("No chats found for user with id ${user.id}") }
            .map { it.toDto() }
    }

    override fun findByChatAndUser(
        chat: ChatDto,
        user: UserDto
    ): ChatMemberDto {
        return chatMemberRepository
            .findByChatAndUser(chat.toEntity(), user.toEntity())
            .orElseThrow { NoSuchElementException("No chat found for user with id ${user.id} in chat with id ${chat.id}") }
            .toDto()
    }

    override fun addMember(
        chatDto :ChatDto,
        userDto: UserDto
    ): ChatMemberDto {
        return save(
           chatMemberDto =  ChatMemberDto(
                chat = chatDto,
                user = userDto
            )
        )
    }

    override fun addMultipleMembers(
        chatDto :ChatDto,
        users: List<UserDto>
    ): List<ChatMemberDto> {
        return users.map {
            addMember(
                chatDto = chatDto,
                userDto = it
            )
        }
    }


    override fun removeMember(
        chatDto: ChatDto,
        userDto: UserDto
    ) {
        delete(
            chatMemberDto = findByChatAndUser(
                chat = chatDto,
                user = userDto
            )
        )
    }

    override fun getChatMembers(
        chat: ChatDto
    ): ChatMembersResponse {
        return ChatMembersResponse(
            chat = chat,
            members = findChatMembers(chat = chat)
        )
    }

    override fun getUserChats(userDto: UserDto): List<ChatDto> {
        return findByUser(user = userDto)
            .mapNotNull { userChat ->
                userChat.chat?.apply {
                    lastMessage = messageService.getLatestMessageOfChat(this)
                }
            }
    }

    override fun findChatMembers(
        chat: ChatDto
    ): List<UserDto> {
        return findByChat(chat = chat)
            .mapNotNull { it.user }
    }

}
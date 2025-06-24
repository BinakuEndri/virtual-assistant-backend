package com.binakuendri.chatbot.services.impl.v1

import com.binakuendri.chatbot.entities.dto.*
import com.binakuendri.chatbot.entities.request.CreateChatRequest
import com.binakuendri.chatbot.entities.request.getChat
import com.binakuendri.chatbot.entities.responses.ChatMembersResponse
import com.binakuendri.chatbot.entities.responses.GetChatsResponse
import com.binakuendri.chatbot.entities.responses.UnReadCountResponse
import com.binakuendri.chatbot.repository.ChatRepository
import com.binakuendri.chatbot.services.*
import com.binakuendri.chatbot.services.auth.JwtService
import com.binakuendri.chatbot.services.impl.PusherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
@Qualifier("v1")
class ChatServiceV1Impl(
    private val pusherService: PusherService,
    @Autowired private val chatRepository: ChatRepository,
    @Autowired private val chatMemberService: ChatMemberService,
    @Autowired private val jwtService: JwtService,
    private val userService: UserService,
    private val messageReceiptService: MessageReceiptService

): ChatService {

    override fun findById(
        chatId: Long
    ): ChatDto {
        return chatRepository
            .findById(chatId = chatId)
            .orElseThrow { NoSuchElementException("Chat not found with id $chatId") }
            .toDto()
    }

    override fun findByChatName(
        chatName: String
    ): ChatDto {
        return chatRepository
            .findByChatName(chatName = chatName)
            .orElse(null)
            .toDto()
    }
    override fun save(
        chat: ChatDto
    ): ChatDto {
        return chatRepository
            .save(chat.toEntity())
            .toDto()
    }

    override fun saveChat(
        createChatRequest: CreateChatRequest
    ): ChatDto{
        var chat = createChatRequest.getChat()

        if (!chat.isGroup) {
            createChatRequest.members.firstOrNull()?.let {
                chat = chat.copy(
                    chatName = userService.findById(id = it).username
                        ?: "Unknown User"
                 )
            }
        }

       return save(chat = chat)
    }

    override fun createChat(
        createChatRequest: CreateChatRequest
    ): ChatDto {
        val savedChat = saveChat(createChatRequest)

        chatMemberService.addMember(
            chatDto = savedChat,
            userDto = jwtService.getUserByToken()
        )

        chatMemberService.addMultipleMembers(
            chatDto = savedChat,
            users = userService.findUsersById(
                usersId = createChatRequest.members
            )
        )

        pusherService.chatEvent(
            members = createChatRequest.members,
            event = "new-chat",
            data = savedChat
        )

        return savedChat
    }


    override fun updateChat(
        id: Long,
        chat: ChatDto
    ): ChatDto {
        if (!chatRepository.existsById(id)) {
            throw NoSuchElementException("Chat not found with id $id")
        }
        return save(chat = chat.copy(id = id))
    }

    override fun deleteChat(
        id: Long
    ) {
        if (!chatRepository.existsById(id)) {
            throw NoSuchElementException("Chat not found with id $id")
        }
        chatRepository.deleteById(id)
    }
    override fun getChatMembers(
        chatId: Long
    ): ChatMembersResponse {
        return chatMemberService.getChatMembers(
            chat = findById(chatId = chatId)
        )
    }

    override fun getChats(
        userDto: UserDto
    ): List<ChatDto> {
        return chatMemberService.getUserChats(userDto = userDto)
    }


    override fun getUserChats(): List<GetChatsResponse> {
        return getChats(userDto =
            jwtService.getUserByToken()
        ).map { chat ->
            GetChatsResponse(
               chat= chat,
               participants = findChatMembers(chat = chat),
               unReadCount = getUnReadChatCount(chatId = chat.id)
            )
        }
    }


    override fun findChatMembers(
        chat: ChatDto
    ): List<UserDto> {
        return chatMemberService.findChatMembers(chat = chat)
    }

    override fun readAllMessagesOfChat(
        chatId: Long
    ): List<MessageReceiptDto> {
        return messageReceiptService.readAllMessages(
            chatDto = findById(chatId = chatId),
            user = jwtService.getUserByToken()
        )
    }

    override fun getUnReadChatCount(
        chatId: Long
    ): Int {
        return messageReceiptService.getUnReadMessagesCount(
           chatDto =  findById(chatId = chatId),
           user =  jwtService.getUserByToken()
        )
    }

    override fun getUnReadCount(): UnReadCountResponse {
       val user = jwtService.getUserByToken()
        return UnReadCountResponse(
            unReadCount = messageReceiptService
                .getUnReadCount(
                    chats = getChats(userDto = user),
                    user = user
            )
        )
    }

    override fun removeMember(
        chatId: Long,
        userId: Int
    ) {
        chatMemberService.removeMember(
           chatDto =  findById(chatId = chatId),
           userDto =  userService.findById(id = userId)
        )
    }

    override fun getUsersToChatWith(): List<UserDto> {
        val currentUser = jwtService.getUserByToken()

        val existingChatNames = getUserChats()
            .map { it.chat.chatName }
            .toSet()

        return userService.getAllUsers()
            .filter { user ->
                user.username != currentUser.username && !existingChatNames.contains(user.username)
            }
    }

}
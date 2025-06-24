package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.chat.Chat
import com.binakuendri.chatbot.entities.chat.ChatMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, Long> {
    fun findByChat(chat: Chat): Optional<List<ChatMember>>
    fun findByUser(user: User): Optional<List<ChatMember>>
    fun findByChatAndUser(chat: Chat, user: User): Optional<ChatMember>
}
package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.chat.Chat
import com.binakuendri.chatbot.entities.chat.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : JpaRepository<Message, Int?> {
    fun findById(messageId: Long?): Optional<Message>
    fun findByChat(chat: Chat): Optional<List<Message>>

}

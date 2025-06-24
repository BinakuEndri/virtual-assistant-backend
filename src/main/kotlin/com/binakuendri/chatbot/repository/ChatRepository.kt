package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.chat.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatRepository : JpaRepository<Chat, Long?> {
    fun findById(chatId: Long?): Optional<Chat>
    fun findByChatName(chatName: String?): Optional<Chat>

}

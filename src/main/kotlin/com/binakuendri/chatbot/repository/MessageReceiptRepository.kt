package com.binakuendri.chatbot.repository

import com.binakuendri.chatbot.entities.auth.User
import com.binakuendri.chatbot.entities.chat.Message
import com.binakuendri.chatbot.entities.chat.MessageReceipt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageReceiptRepository : JpaRepository<MessageReceipt, Int?> {
    fun findById(messageId: Long?): Optional<MessageReceipt>
    fun findByMessage(message: Message): Optional<List<MessageReceipt>>
    fun findByUser(user: User): Optional<List<MessageReceipt>>
    fun findByMessageAndUser(message: Message, user: User): Optional<MessageReceipt>
}

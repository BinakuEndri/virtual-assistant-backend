package com.binakuendri.chatbot.entities.chat

import com.binakuendri.chatbot.entities.auth.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message_receipts")
data class MessageReceipt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    val message: Message? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null,

    var readAt: LocalDateTime? = null
)
package com.binakuendri.chatbot.entities.chat

import com.binakuendri.chatbot.entities.auth.User
import jakarta.persistence.*
import java.time.Instant
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "messages")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    var chat: Chat? = null,

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    var sender: User? = null,

    @Column(nullable = false)
    var content: String = "",

    var sentAt: Instant? =Instant.now()
)
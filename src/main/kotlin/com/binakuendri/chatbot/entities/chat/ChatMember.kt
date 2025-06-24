package com.binakuendri.chatbot.entities.chat

import com.binakuendri.chatbot.entities.auth.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_members")
data class ChatMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "chat_id")
    val chat: Chat? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    val joinedAt: LocalDateTime? = LocalDateTime.now()
)
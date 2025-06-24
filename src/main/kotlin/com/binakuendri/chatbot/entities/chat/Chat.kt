package com.binakuendri.chatbot.entities.chat

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val chatName: String? = null,

    val isGroup: Boolean = false,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
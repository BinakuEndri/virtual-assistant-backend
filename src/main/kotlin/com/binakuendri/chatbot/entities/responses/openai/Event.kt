package com.binakuendri.chatbot.entities.responses.openai

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(
    val id: String,
    val objectType: String,
    val createdAt: Long,
    val assistantId: String,
    val threadId: String,
    val status: String,
    val stepDetails: StepDetails?,
    val message: Message?,
    val delta: Delta?
)

data class StepDetails(
    val type: String,
    val messageCreation: MessageCreation?
)

data class MessageCreation(
    val messageId: String
)

data class Message(
    val content: List<Content>?
)

data class Delta(
    val content: List<Content>?
)

data class Content(
    val type: String,
    val text: Text?
)

data class Text(
    val value: String
)
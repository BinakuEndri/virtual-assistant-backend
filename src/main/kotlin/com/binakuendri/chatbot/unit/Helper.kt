package com.binakuendri.chatbot.unit

import com.binakuendri.chatbot.entities.responses.openai.Event
import com.binakuendri.chatbot.entities.responses.openai.StreamData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.json.JSONObject


fun extractContentFromEvent(event: String): String {
    val objectMapper = jacksonObjectMapper()

    // Parse the JSON string into the Event class
    val parsedEvent: Event = objectMapper.readValue(event, Event::class.java)

    // Check if the event contains a delta (partial message) or a completed message
    val content = StringBuilder()

    // Extract content from the delta if it exists
    parsedEvent.delta?.content?.forEach { contentItem ->
        contentItem.text?.value?.let {
            content.append(it)
        }
    }

    // Or if it's a completed message, extract the full message content
    parsedEvent.message?.content?.forEach { contentItem ->
        contentItem.text?.value?.let {
            content.append(it)
        }
    }

    return content.toString()
}

fun parseEvent(event: String): StreamData {
    val json = JSONObject(event)

    val status = json.optString("status", "unknown")

    val obj = json.optString("object")

    var runId : String? = null

    when (obj){
        "thread.run" ->{
            runId = json.optString("id")
        }
    }

    val content = json.optJSONArray("content")
        ?.optJSONObject(0)
        ?.optJSONObject("text")
        ?.optString("value", null)

    return StreamData(runId,status,content,obj)
}
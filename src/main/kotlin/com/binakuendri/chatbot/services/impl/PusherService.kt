package com.binakuendri.chatbot.services.impl

import com.pusher.rest.Pusher
import org.springframework.stereotype.Service

@Service
class PusherService(private val pusher: Pusher) {

    fun triggerEvent(channel: String, event: String, data: Any) {
        pusher.trigger(channel, event, data)
    }

    fun chatEvent(members :List<Int>,event : String,data : Any){
        try {
            members.forEach { userId ->
                val channelName = "chatbot.user.$userId"
                triggerEvent(channelName, event, data)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
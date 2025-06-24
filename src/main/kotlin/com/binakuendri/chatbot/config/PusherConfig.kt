package com.binakuendri.chatbot.config

import com.pusher.rest.Pusher
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PusherConfig {

    private val dotenv = Dotenv.load()
    private val appId: String = dotenv["PUSHER_APP_ID"]
        ?: throw IllegalStateException("Missing PUSHER_APP_ID")

    private val key: String = dotenv["PUSHER_KEY"]
        ?: throw IllegalStateException("Missing PUSHER_KEY")

    private val secret: String = dotenv["PUSHER_SECRET"]
        ?: throw IllegalStateException("Missing PUSHER_SECRET")

    @Bean
    fun pusher(): Pusher {
        val pusher =  Pusher(
            appId,
            key,
            secret,
        )
        pusher.setCluster("eu")
        pusher.setEncrypted(true)
        return pusher
    }
}
package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import jakarta.websocket.*
import kotlinx.coroutines.delay

@Suppress("unused", "UNUSED_PARAMETER")
@ClientEndpoint
class TestWebSocketV2Client {
    var session: Session? = null
    private val messages: MutableList<String> = mutableListOf()

    @OnOpen
    fun onOpen(session: Session?) {
        this.session = session
    }

    @OnClose
    fun onClose(session: Session?, reason: CloseReason) {
        this.session = null
    }

    @OnMessage
    fun onMessage(message: String) {
        messages.add(message)
    }

    suspend fun receive(): String {
        while (messages.isEmpty()) {
            delay(100)
        }
        return messages.removeFirst()
    }
}

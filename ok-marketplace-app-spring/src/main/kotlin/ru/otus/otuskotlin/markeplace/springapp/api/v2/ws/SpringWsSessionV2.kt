package ru.otus.otuskotlin.markeplace.springapp.api.v2.ws

import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession

data class SpringWsSessionV2(
    private val session: WebSocketSession,
) : IMkplWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV2ResponseSerialize(obj)
        session.sendMessage(TextMessage(message))
    }
}

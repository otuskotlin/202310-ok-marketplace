package ru.otus.otuskotlin.markeplace.springapp.api.v1.ws

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.fakeMkplPrincipal
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit

@Component
class WsAdHandlerV1(private val appSettings: MkplAppSettings) : TextWebSocketHandler() {
    private val sessions = appSettings.corSettings.wsSessions

    override fun afterConnectionEstablished(sess: WebSocketSession) = runBlocking {
        val session = SpringWsSessionV1(sess)
        sessions.add(session)

        appSettings.controllerHelper(
            {
                command = MkplCommand.INIT
                principal = fakeMkplPrincipal()
            },
            { session.send(toTransportInit()) },
            WsAdHandlerV1::class,
            "ws-v1-init"
        )
    }

    override fun handleTextMessage(sess: WebSocketSession, message: TextMessage) = runBlocking {
        val session = SpringWsSessionV1(sess)
        appSettings.controllerHelper(
            {
                val request = apiV1Mapper.readValue(message.payload, IRequest::class.java)
                fromTransport(request)
                principal = fakeMkplPrincipal()
            },
            { session.send(toTransportAd()) },
            WsAdHandlerV1::class,
            "ws-v1-handle"
        )
    }

    override fun afterConnectionClosed(sess: WebSocketSession, status: CloseStatus): Unit = runBlocking {
        val session = SpringWsSessionV1(sess)
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            {},
            WsAdHandlerV1::class,
            "ws-v1-finish"
        )
        sessions.remove(session)
    }
}

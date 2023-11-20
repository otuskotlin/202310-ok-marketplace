package ru.otus.otuskotlin.markeplace.springapp.api.v2.ws

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.fakeMkplPrincipal
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

@Component
class WsAdHandlerV2(private val appSettings: MkplAppSettings) : TextWebSocketHandler() {
    private val sessions = appSettings.corSettings.wsSessions

    override fun afterConnectionEstablished(sess: WebSocketSession) = runBlocking {
        val session = SpringWsSessionV2(sess)
        sessions.add(session)

        appSettings.controllerHelper(
            {
                command = MkplCommand.INIT
                principal = fakeMkplPrincipal()
            },
            { session.send(toTransportInit()) },
            WsAdHandlerV2::class,
            "ws-v2-init",
        )
    }

    override fun handleTextMessage(sess: WebSocketSession, message: TextMessage) = runBlocking {
        val session = SpringWsSessionV2(sess)
        appSettings.controllerHelper(
            {
                val request = apiV2Mapper.decodeFromString<IRequest>(message.payload)
                fromTransport(request)
                principal = fakeMkplPrincipal()
            },
            { session.send(toTransportAd()) },
            WsAdHandlerV2::class,
            "ws-v2-handle",
        )
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = runBlocking {
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            {},
            WsAdHandlerV2::class,
            "ws-v2-finish",
            )
        sessions.remove(SpringWsSessionV2(session))
    }
}

package ru.otus.otuskotlin.markeplace.springapp.api.v1.ws

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.base.SpringWsSessionRepo
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit

@Component
class WsAdHandlerV1(
    private val processor: MkplAdProcessor,
    private val sessions: SpringWsSessionRepo
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(sess: WebSocketSession) = runBlocking {
        val session = SpringWsSessionV1(sess)
        sessions.add(session)

        val context = MkplContext()
        // TODO убрать, когда заработает обычный режим
        context.workMode = MkplWorkMode.STUB
        processor.exec(context)
        session.send(context.toTransportInit())
    }

    override fun handleTextMessage(sess: WebSocketSession, message: TextMessage) = runBlocking {
        val session = SpringWsSessionV1(sess)
        val ctx = MkplContext(timeStart = Clock.System.now())
        try {
            val request = apiV1Mapper.readValue(message.payload, IRequest::class.java)
            ctx.fromTransport(request)
            processor.exec(ctx)
            session.send(ctx.toTransportAd())
        } catch (e: Exception) {
            ctx.errors.add(e.asMkplError())
            session.send(ctx.toTransportInit())
        }
    }

    override fun afterConnectionClosed(sess: WebSocketSession, status: CloseStatus) {
        val session = SpringWsSessionV1(sess)
        sessions.remove(session)
    }
}

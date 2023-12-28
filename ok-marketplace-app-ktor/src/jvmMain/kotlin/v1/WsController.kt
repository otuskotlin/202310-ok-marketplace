package ru.otus.otuskotlin.marketplace.app.ktor.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionV1
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v1.*

suspend fun WebSocketSession.wsHandlerV1(processor: MkplAdProcessor, sessions: KtorWsSessionRepo) = with(KtorWsSessionV1(this)) {
    sessions.add(this)

    // Handle init request
    val ctx = MkplContext()
    ctx.workMode = MkplWorkMode.STUB
    processor.exec(ctx)
    val init = apiV1Mapper.writeValueAsString(ctx.toTransportInit())
    outgoing.send(Frame.Text(init))

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = MkplContext()

        // Handle without flow destruction
        try {
            val request = apiV1Mapper.readValue<IRequest>(jsonStr)
            context.fromTransport(request)
            processor.exec(context)

            val result = apiV1Mapper.writeValueAsString(context.toTransportAd())

            outgoing.send(Frame.Text(result))
        } catch (_: ClosedReceiveChannelException) {
            sessions.clearAll()
        } catch (t: Throwable) {
            context.addError(t.asMkplError())

            val result = apiV1Mapper.writeValueAsString(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}

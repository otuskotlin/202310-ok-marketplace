package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionV2
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

suspend fun WebSocketSession.wsHandlerV2(processor: MkplAdProcessor, sessions: KtorWsSessionRepo) = with(KtorWsSessionV2(this)) {
    sessions.add(this)

    // Handle init request
    run {
        val ctx = MkplContext(session = this)
        ctx.workMode = MkplWorkMode.STUB
        processor.exec(ctx)

        val init = apiV2ResponseSerialize(ctx.toTransportInit())
        outgoing.send(Frame.Text(init))
    }

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull

        val jsonStr = frame.readText()
        val context = MkplContext(session = this)

        // Handle without flow destruction
        try {
            val request = apiV2Mapper.decodeFromString<IRequest>(jsonStr)
            context.fromTransport(request)
            processor.exec(context)

            val result = apiV2ResponseSerialize(context.toTransportAd())

            outgoing.send(Frame.Text(result))
        } catch (_: ClosedReceiveChannelException) {
            sessions.clearAll()
        } catch (t: Throwable) {
            context.addError(t.asMkplError())

            val result = apiV2ResponseSerialize(context.toTransportInit())
            outgoing.send(Frame.Text(result))
        }
    }.collect()
}

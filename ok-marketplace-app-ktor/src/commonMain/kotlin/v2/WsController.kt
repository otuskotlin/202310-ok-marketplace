package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionV2
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

suspend fun WebSocketSession.wsHandlerV2(appSettings: MkplAppSettings) = with(KtorWsSessionV2(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        { command = MkplCommand.INIT },
        { outgoing.send(Frame.Text(apiV2ResponseSerialize(toTransportInit()))) }
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                { fromTransport(apiV2RequestDeserialize<IRequest>(frame.readText())) },
                {
                    val result = apiV2ResponseSerialize(toTransportAd())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                }
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.clearAll()
        } catch (e: Throwable) {
            println("FFF")
        }

        // Handle finish request
        appSettings.controllerHelper(
            { command = MkplCommand.FINISH },
            { }
        )
    }.collect()
}

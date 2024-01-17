package ru.otus.otuskotlin.marketplace.app.ktor.v1

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionV1
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit

suspend fun WebSocketSession.wsHandlerV1(appSettings: MkplAppSettings) = with(KtorWsSessionV1(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        { command = MkplCommand.INIT },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) }
    )

    // Handle flow
    incoming.receiveAsFlow().mapNotNull { it ->
        val frame = it as? Frame.Text ?: return@mapNotNull
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                { fromTransport(apiV1Mapper.readValue<IRequest>(frame.readText())) },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportAd())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                }
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.clearAll()
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                { command = MkplCommand.FINISH },
                { }
            )
        }
    }.collect()
}

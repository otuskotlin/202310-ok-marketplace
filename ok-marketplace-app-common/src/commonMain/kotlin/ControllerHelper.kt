package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState

suspend inline fun <T> IMkplAppSettings.controllerHelper(
    getRequest: MkplContext.() -> Unit,
    toResponse: MkplContext.() -> T,
): T {
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = MkplState.FAILING
        ctx.errors.add(e.asMkplError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}

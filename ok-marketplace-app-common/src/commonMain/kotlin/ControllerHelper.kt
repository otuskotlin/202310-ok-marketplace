package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import kotlin.reflect.KClass

suspend inline fun <T> IMkplAppSettings.controllerHelper(
    crossinline getRequest: suspend MkplContext.() -> Unit,
    crossinline toResponse: suspend MkplContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MkplContext(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.doWithLogging(logId) {
            ctx.getRequest()
            processor.exec(ctx)
            logger.info(
                msg = "Request $logId processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            ctx.state = MkplState.FAILING
            ctx.errors.add(e.asMkplError())
            processor.exec(ctx)
            ctx.toResponse()
        }
    }
}

package ru.otus.otuskotlin.marketplace.logging.socket

import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import kotlin.reflect.KClass

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
fun mpLoggerSocket(loggerId: String): IMpLogWrapper = MpLoggerWrapperSocket(
//        logger = logger,
    loggerId = loggerId,
)

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
fun mpLoggerSocket(cls: KClass<*>): IMpLogWrapper = MpLoggerWrapperSocket(
//        logger = logger,
    loggerId = cls.qualifiedName ?: "",
)

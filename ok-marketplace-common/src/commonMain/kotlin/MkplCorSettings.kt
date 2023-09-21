package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSessionRepo
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplCorSettings(
    val wsSessions: IMkplWsSessionRepo = IMkplWsSessionRepo.NONE,
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}

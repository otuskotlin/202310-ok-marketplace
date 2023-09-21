package ru.otus.otuskotlin.marketplace.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.base.KtorWsSessionRepo
import ru.otus.otuskotlin.marketplace.app.ktor.plugins.getLoggerProviderConf
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings


fun Application.initAppSettings(): MkplAppSettings {
    return MkplAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = MkplAdProcessor(),
        corSettings = MkplCorSettings(
            wsSessions = KtorWsSessionRepo(),
            loggerProvider = getLoggerProviderConf(),
        ),
    )
}

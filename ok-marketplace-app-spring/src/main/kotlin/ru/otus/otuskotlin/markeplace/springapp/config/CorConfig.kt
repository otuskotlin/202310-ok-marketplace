package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.markeplace.springapp.base.SpringWsSessionRepo
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback

@Suppress("unused")
@Configuration
class CorConfig {
    @Bean
    fun processor() = MkplAdProcessor()

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings() = MkplCorSettings(
        wsSessions = wsRepo(),
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings() = MkplAppSettings(
        processor = processor(),
        corSettings = corSettings(),
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()
}

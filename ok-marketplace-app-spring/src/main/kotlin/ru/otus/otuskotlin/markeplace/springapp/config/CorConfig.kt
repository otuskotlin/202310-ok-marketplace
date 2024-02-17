package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.markeplace.springapp.base.SpringWsSessionRepo
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

@Suppress("unused")
@Configuration
class CorConfig {
    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun prodRepository() = AdRepoInMemory()

    @Bean
    fun testRepository() = AdRepoInMemory()

    @Bean
    fun stubRepository() = AdRepoStub()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IAdRepository,
        @Qualifier("testRepository") testRepository: IAdRepository,
        @Qualifier("stubRepository") stubRepository: IAdRepository,
    ): MkplCorSettings = MkplCorSettings(
        wsSessions = wsRepo(),
        loggerProvider = loggerProvider(),
        repoStub = stubRepository,
        repoTest = testRepository,
        repoProd = prodRepository,
    )

    @Bean
    fun appSettings(corSettings: MkplCorSettings, processor: MkplAdProcessor) = MkplAppSettings(
        corSettings = corSettings,
        processor = processor,
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()
}

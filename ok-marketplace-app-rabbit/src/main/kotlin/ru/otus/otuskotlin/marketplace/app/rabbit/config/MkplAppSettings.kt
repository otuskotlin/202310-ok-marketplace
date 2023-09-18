package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    val config: RabbitConfig = RabbitConfig(),
    override val processor: MkplAdProcessor = MkplAdProcessor(),
    override val corSettings: MkplCorSettings = MkplCorSettings(),
): IMkplAppSettings

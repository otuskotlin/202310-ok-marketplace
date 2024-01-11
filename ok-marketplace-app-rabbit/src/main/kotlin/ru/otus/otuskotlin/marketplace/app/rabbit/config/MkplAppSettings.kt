package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

data class MkplAppSettings(
    val config: RabbitConfig = RabbitConfig(),
    val processor: MkplAdProcessor = MkplAdProcessor(),
)

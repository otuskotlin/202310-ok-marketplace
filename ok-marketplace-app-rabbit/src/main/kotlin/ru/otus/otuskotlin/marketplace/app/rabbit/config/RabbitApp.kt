package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.marketplace.app.rabbit.processor.RabbitDirectProcessorV1
import ru.otus.otuskotlin.marketplace.app.rabbit.processor.RabbitDirectProcessorV2

data class RabbitApp(
    val appSettings: MkplAppSettings = MkplAppSettings(),
    val producerConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange",
        queue = "v1-queue",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    ),
    val producerConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration(
        keyIn = "in-v2",
        keyOut = "out-v2",
        exchange = "transport-exchange",
        queue = "v2-queue",
        consumerTag = "v2-consumer",
        exchangeType = "direct"
    ),
    val processor1: RabbitDirectProcessorV1 = RabbitDirectProcessorV1(
        processorConfig = producerConfigV1,
        appSettings = appSettings,
    ),
    val processor2: RabbitDirectProcessorV2 = RabbitDirectProcessorV2(
        processorConfig = producerConfigV2,
        appSettings = appSettings,
    ),
    val controller: RabbitController = RabbitController(
        processors = setOf(processor1, processor2)
    )
)

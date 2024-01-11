package ru.otus.otuskotlin.marketplace.app.rabbit

import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitApp
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig

fun main(vararg args: String) {
    println("starting Application")
    val rabbitConfig = RabbitConfig(*args)
    println("rabbitConfig: $rabbitConfig")
    val appSettings = MkplAppSettings(config = rabbitConfig)
    val app = RabbitApp(appSettings = appSettings)
    app.controller.start()
}

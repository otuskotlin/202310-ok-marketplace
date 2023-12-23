package ru.otus.otuskotlin.marketplace.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.yaml.*
import io.ktor.server.engine.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.staticCFunction
import platform.posix.*

fun signalHook(sig: Int) {
    println("Signal $sig caught")
    exit(0)
}

@OptIn(ExperimentalForeignApi::class)
fun main() {
    signal(SIGINT, staticCFunction(::signalHook))
    signal(SIGTERM, staticCFunction(::signalHook))
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        val conf = YamlConfigLoader().load("./application.yaml")
            ?: throw RuntimeException("Cannot read application.yaml")
        println(conf)
        config = conf
        println("File read")

        module {
            module()
        }

        connector {
            port = conf.port
            host = conf.host
        }
        println("Starting")
        signal(SIGINT, staticCFunction(::signalHook))
    }).start(true)
}

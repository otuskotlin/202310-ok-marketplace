package ru.otus.otuskotlin.marketplace.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.app.ktor.v2.v2Ad
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

fun Application.module(processor: MkplAdProcessor = MkplAdProcessor()) {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
            v2Ad(processor)
        }
    }
}

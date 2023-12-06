package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.json.Json

@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
    ignoreUnknownKeys = true
}

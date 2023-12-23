package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.json.Json
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse

@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {
//    ignoreUnknownKeys = true
}

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(obj: IResponse): String =
    apiV2Mapper.encodeToString(IResponse.serializer(), obj)

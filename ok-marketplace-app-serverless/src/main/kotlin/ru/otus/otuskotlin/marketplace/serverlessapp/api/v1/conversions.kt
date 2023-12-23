package ru.otus.otuskotlin.marketplace.serverlessapp.api.v1

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplRequestId
import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Request
import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Response
import ru.otus.otuskotlin.marketplace.serverlessapp.api.utils.toYResponse
import yandex.cloud.sdk.functions.Context
import java.util.*

inline fun <reified T> Request.decodeFromY(): T? = body?.let { bd ->
    apiV1RequestDeserialize(
        if (isBase64Encoded) {
            (Base64.getDecoder().decode(bd).toString(Charsets.UTF_8))
        } else {
            bd
        }
    )
}

fun withContext(context: Context, block: MkplContext.() -> Response) =
    with(
        MkplContext(
            timeStart = Clock.System.now(),
            requestId = MkplRequestId(context.requestId)
        )
    ) {
        block()
    }

/**
 * V1
 */
fun ru.otus.otuskotlin.marketplace.api.v1.models.IResponse.toResponse(): Response =
    toYResponse(apiV1ResponseSerialize(this))

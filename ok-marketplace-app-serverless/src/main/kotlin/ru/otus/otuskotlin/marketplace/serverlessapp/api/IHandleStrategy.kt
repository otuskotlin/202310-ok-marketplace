package ru.otus.otuskotlin.marketplace.serverlessapp.api

import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Request
import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Response
import yandex.cloud.sdk.functions.Context

interface IHandleStrategy {
    val path: String
    fun handle(req: Request, reqContext: Context): Response
}

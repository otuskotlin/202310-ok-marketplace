package ru.otus.otuskotlin.marketplace.serverlessapp.api.utils

import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Response

/**
 * Input:    /v1/ad/create?
 * Output:  ad/create
 */
fun String.dropVersionPrefix(versionPrefix: String) = replace("/$versionPrefix/", "")

fun toYResponse(body: String): Response =
    Response(
        200,
        false,
        mapOf("Content-Type" to "application/json"),
        body,
    )

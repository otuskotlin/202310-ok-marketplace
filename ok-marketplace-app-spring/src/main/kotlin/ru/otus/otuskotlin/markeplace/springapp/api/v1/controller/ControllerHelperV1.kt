package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV1(
    appSettings: MkplAppSettings,
    request: Q,
): R = appSettings.controllerHelper(
    { fromTransport(request) },
    { toTransportAd() as R }
)

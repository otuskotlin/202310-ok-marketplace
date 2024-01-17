package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.controllerHelper
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV2(
    appSettings: MkplAppSettings,
    request: Q,
): R = appSettings.controllerHelper(
    { fromTransport(request) },
    { toTransportAd() as R }
)

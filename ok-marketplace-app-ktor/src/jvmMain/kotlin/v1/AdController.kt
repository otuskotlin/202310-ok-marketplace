package ru.otus.otuskotlin.marketplace.app.ktor.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.v1.processV1

suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) =
    processV1<AdCreateRequest, AdCreateResponse>(appSettings)

suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) =
    processV1<AdReadRequest, AdReadResponse>(appSettings)

suspend fun ApplicationCall.updateAd(appSettings: MkplAppSettings) =
    processV1<AdUpdateRequest, AdUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteAd(appSettings: MkplAppSettings) =
    processV1<AdDeleteRequest, AdDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchAd(appSettings: MkplAppSettings) =
    processV1<AdSearchRequest, AdSearchResponse>(appSettings)

suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) =
    processV1<AdOffersRequest, AdOffersResponse>(appSettings)

package ru.otus.otuskotlin.marketplace.app.ktor.v2

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings

suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) =
    processV2<AdCreateRequest, AdCreateResponse>(appSettings)

suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) =
    processV2<AdReadRequest, AdReadResponse>(appSettings)

suspend fun ApplicationCall.updateAd(appSettings: MkplAppSettings) =
    processV2<AdUpdateRequest, AdUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteAd(appSettings: MkplAppSettings) =
    processV2<AdDeleteRequest, AdDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchAd(appSettings: MkplAppSettings) =
    processV2<AdSearchRequest, AdSearchResponse>(appSettings)

suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) =
    processV2<AdOffersRequest, AdOffersResponse>(appSettings)

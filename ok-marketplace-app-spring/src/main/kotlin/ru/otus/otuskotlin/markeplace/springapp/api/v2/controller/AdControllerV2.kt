package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.models.*

@Suppress("unused")
@RestController
@RequestMapping("v2/ad")
class AdControllerV2(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    suspend fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse =
        processV2(appSettings, request = request, AdControllerV2::class, "create")

    @PostMapping("read")
    suspend fun  readAd(@RequestBody request: AdReadRequest): AdReadResponse =
        processV2(appSettings, request = request, AdControllerV2::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse =
        processV2(appSettings, request = request, AdControllerV2::class, "update")

    @PostMapping("delete")
    suspend fun  deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse =
        processV2(appSettings, request = request, AdControllerV2::class, "delete")

    @PostMapping("search")
    suspend fun  searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse =
        processV2(appSettings, request = request, AdControllerV2::class, "search")
}

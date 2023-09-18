package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersResponse

@Suppress("unused")
@RestController
@RequestMapping("v2/ad")
class OfferControllerV2(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("offers")
    suspend fun searchOffers(@RequestBody request: AdOffersRequest): AdOffersResponse =
        processV2(appSettings, request = request)
}

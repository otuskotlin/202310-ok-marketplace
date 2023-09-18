package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse

@Suppress("unused")
@RestController
@RequestMapping("v1/ad")
class OfferControllerV1(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("offers")
    suspend fun searchOffers(@RequestBody request: AdOffersRequest): AdOffersResponse =
        processV1(appSettings, request = request)
}

package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.markeplace.springapp.config.CorConfig
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.backend.repo.sql.RepoAdSQL
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(AdControllerV1::class, OfferControllerV1::class, CorConfig::class)
internal class AdControllerTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: MkplAdProcessor

    @Suppress("unused")
    @MockBean
    private lateinit var repo: RepoAdSQL

    @Test
    fun createAd() = testStubAd(
        "/v1/ad/create",
        AdCreateRequest(),
        MkplContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readAd() = testStubAd(
        "/v1/ad/read",
        AdReadRequest(),
        MkplContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateAd() = testStubAd(
        "/v1/ad/update",
        AdUpdateRequest(),
        MkplContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteAd() = testStubAd(
        "/v1/ad/delete",
        AdDeleteRequest(),
        MkplContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchAd() = testStubAd(
        "/v1/ad/search",
        AdSearchRequest(),
        MkplContext().toTransportSearch().copy(responseType = "search")
    )

    @Test
    fun searchOffers() = testStubAd(
        "/v1/ad/offers",
        AdOffersRequest(),
        MkplContext().toTransportOffers().copy(responseType = "offers")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubAd(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}

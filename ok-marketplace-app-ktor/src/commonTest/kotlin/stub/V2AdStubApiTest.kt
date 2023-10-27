package ru.otus.otuskotlin.marketplace.app.ktor.stub

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.module
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V2AdStubApiTest {

    @Test
    fun create() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "12345",
                ad = AdCreateObject(
                    title = "Болт",
                    description = "КРУТЕЙШИЙ",
                    adType = DealSide.DEMAND,
                    visibility = AdVisibility.PUBLIC,
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdCreateResponse
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun read() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = AdReadObject("666"),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdReadResponse
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun update() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = AdUpdateObject(
                    id = "666",
                    title = "Болт",
                    description = "КРУТЕЙШИЙ",
                    adType = DealSide.DEMAND,
                    visibility = AdVisibility.PUBLIC,
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdUpdateResponse
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun delete() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = "666",
                    lock = "123"
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdDeleteResponse
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun search() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdSearchResponse
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = v2TestApplication {  client ->
        val response = client.post("/v2/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = "12345",
                ad = AdReadObject(
                    id = "666"
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<IResponse>() as AdOffersResponse
        assertEquals(200, response.status.value)
        assertEquals("s-666-01", responseObj.ads?.first()?.id)
    }

    private fun v2TestApplication(function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application { module(MkplAppSettings(corSettings = MkplCorSettings(repoStub = AdRepoStub()))) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }
        }
        function(client)
    }

}

package ru.otus.otuskotlin.marketplace.app.ktor.stub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V1AdStubApiTest {
    @Test
    fun create() = v1TestApplication { client ->
        val response = client.post("/v1/ad/create") {
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
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }


    @Test
    fun read() = v1TestApplication { client ->
        val response = client.post("/v1/ad/read") {
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
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun update() = v1TestApplication { client ->
        val response = client.post("/v1/ad/update") {
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
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun delete() = v1TestApplication { client ->
        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = "666",
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("666", responseObj.ad?.id)
    }

    @Test
    fun search() = v1TestApplication { client ->
        val response = client.post("/v1/ad/search") {
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
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("d-666-01", responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = v1TestApplication { client ->
        val response = client.post("/v1/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = "12345",
                ad = AdReadObject(
                    id = "666",
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.STUB,
                    stub = AdRequestDebugStubs.SUCCESS
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdOffersResponse>()
        assertEquals(200, response.status.value)
        assertEquals("s-666-01", responseObj.ads?.first()?.id)
    }

    private fun v1TestApplication(function: suspend (HttpClient) -> Unit): Unit = testApplication {
        application { moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoStub = AdRepoStub()))) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        function(client)
    }
}

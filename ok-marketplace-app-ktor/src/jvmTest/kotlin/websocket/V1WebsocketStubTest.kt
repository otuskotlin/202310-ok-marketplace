package ru.otus.otuskotlin.marketplace.app.ktor.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = AdCreateRequest(
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

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun readStub() {
        val request = AdReadRequest(
            requestId = "12345",
            ad = AdReadObject("666"),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun updateStub() {
        val request = AdUpdateRequest(
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

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun deleteStub() {
        val request = AdDeleteRequest(
            requestId = "12345",
            ad = AdDeleteObject(
                id = "666",
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun searchStub() {
        val request = AdSearchRequest(
            requestId = "12345",
            adFilter = AdSearchFilter(),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun offersStub() {
        val request = AdOffersRequest(
            requestId = "12345",
            ad = AdReadObject(
                id = "666",
            ),
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoStub = AdRepoStub()))) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<AdInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3000) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}

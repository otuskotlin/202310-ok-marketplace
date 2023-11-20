package ru.otus.otuskotlin.marketplace.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = AdCreateRequest(
        requestId = "1234",
        ad = AdCreateObject(
            title = "some ad",
            description = "some description of some ad",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
            productId = "some product id",
        ),
        debug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IMkplAppSettings = object : IMkplAppSettings {
        override val corSettings: MkplCorSettings = MkplCorSettings()
        override val auth: AuthConfig = AuthConfig.NONE
        override val processor: MkplAdProcessor = MkplAdProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: AdCreateRequest): AdCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportAd() as AdCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IMkplAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<AdCreateRequest>()) },
            { toTransportAd() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as AdCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}

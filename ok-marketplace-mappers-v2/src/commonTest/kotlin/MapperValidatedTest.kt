package ru.otus.otuskotlin.marketplace.mappers.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugStubs
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperValidatedTest {
    @Test
    fun fromTransportValidated() {
        val req = AdCreateRequest(
            requestId = "1234",
            debug = AdDebug(
                stub = AdRequestDebugStubs.SUCCESS,
            ),
        )

        val context = MkplContext()
        context.fromTransportValidated(req)

        assertEquals(MkplStubs.SUCCESS, context.stubCase)
    }
}

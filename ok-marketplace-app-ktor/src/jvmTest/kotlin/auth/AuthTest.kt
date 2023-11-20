package ru.otus.otuskotlin.marketplace.app.auth

import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig
import ru.otus.otuskotlin.marketplace.app.ktor.auth.addAuth
import ru.otus.otuskotlin.marketplace.app.ktor.helpers.testSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            moduleJvm(testSettings())
        }

        val response = client.post("/v1/ad/create") {
            addAuth(config = AuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}

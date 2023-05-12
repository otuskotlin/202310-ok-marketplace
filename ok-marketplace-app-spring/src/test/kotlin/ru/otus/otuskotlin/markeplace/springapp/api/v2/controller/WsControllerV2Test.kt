package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import io.kotest.common.runBlocking
import jakarta.websocket.ContainerProvider
import jakarta.websocket.WebSocketContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.models.AdInitResponse
import ru.otus.otuskotlin.marketplace.api.v2.models.ResponseResult
import java.net.URI


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WsControllerV2Test {

    @LocalServerPort
    private var port: Int = 0

    private lateinit var container: WebSocketContainer
    private lateinit var client: TestWebSocketV2Client

    @BeforeEach
    fun setup() {
        container = ContainerProvider.getWebSocketContainer()
        client = TestWebSocketV2Client()
    }

    @Test
    fun initSession() {
        runBlocking {
            withContext(Dispatchers.IO) {
                container.connectToServer(client, URI.create("ws://localhost:${port}/v2/ws"))
            }

            withTimeout(3000) {
                while (client.session?.isOpen != true) {
                    delay(100)
                }
            }
            assert(client.session?.isOpen == true)
            withTimeout(3000) {
                val incame = client.receive()
                val message = apiV2ResponseDeserialize<AdInitResponse>(incame)
                assert(message.result == ResponseResult.SUCCESS)
            }
        }
    }
}


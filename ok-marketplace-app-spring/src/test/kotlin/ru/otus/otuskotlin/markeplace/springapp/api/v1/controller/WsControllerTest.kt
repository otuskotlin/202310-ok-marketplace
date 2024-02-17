package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import jakarta.websocket.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.AdInitResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.ResponseResult
import java.net.URI
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Suppress("unused")
class WsControllerTest {

    @LocalServerPort
    private var port: Int = 0

    private lateinit var container: WebSocketContainer
    private lateinit var client: TestWebSocketClient

    @BeforeEach
    fun setup() {
        container = ContainerProvider.getWebSocketContainer()
        client = TestWebSocketClient()
    }

    @Test
    fun initSession() {
        runBlocking {
            withContext(Dispatchers.IO) {
                container.connectToServer(client, URI.create("ws://localhost:${port}/v1/ws"))
            }

            withTimeout(3000) {
                while (client.session?.isOpen != true) {
                    delay(100)
                }
            }
            assert(client.session?.isOpen == true)
            withTimeout(3000) {
                val incame = client.receive()
                val message = apiV1Mapper.readValue(incame, IResponse::class.java)
                assert(message is AdInitResponse)
                assert(message.result == ResponseResult.SUCCESS)
            }
        }
    }
}

@Suppress("unused", "UNUSED_PARAMETER")
@ClientEndpoint
class TestWebSocketClient {
    var session: Session? = null
    private val messages: MutableList<String> = mutableListOf()

    @OnOpen
    fun onOpen(session: Session?) {
        this.session = session
    }

    @OnClose
    fun onClose(session: Session?, reason: CloseReason) {
        this.session = null
    }

    @OnMessage
    fun onMessage(message: String) {
        messages.add(message)
    }

    suspend fun receive(): String {
        while (messages.isEmpty()) {
            delay(100)
        }
        return messages.removeFirst()
    }
}

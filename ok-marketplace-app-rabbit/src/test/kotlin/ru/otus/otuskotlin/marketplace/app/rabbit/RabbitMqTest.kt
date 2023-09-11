package ru.otus.otuskotlin.marketplace.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateObject
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugMode
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugStubs
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitApp
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateObject as AdCreateObjectV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateRequest as AdCreateRequestV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateResponse as AdCreateResponseV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdDebug as AdDebugV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugMode as AdRequestDebugModeV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugStubs as AdRequestDebugStubsV2

//  тесты в использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
                withExposedPorts(5672, 15672)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
        }
        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = MkplAppSettings(
        config = RabbitConfig(
            port = container.getMappedPort(5672)
        ),
    )
    private val app by lazy {
        RabbitApp(
            appSettings = appSettings,
            producerConfigV1 = RabbitExchangeConfiguration(
                keyIn = "in-v1",
                keyOut = "out-v1",
                exchange = exchange,
                queue = "v1-queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            ),
            producerConfigV2 = RabbitExchangeConfiguration(
                keyIn = "in-v2",
                keyOut = "out-v2",
                exchange = exchange,
                queue = "v2-queue",
                consumerTag = "v2-consumer",
                exchangeType = exchangeType
            ),
        )
    }

    @BeforeTest
    fun tearUp() {
        app.controller.start()
    }

    @AfterTest
    fun tearDown() {
        app.controller.close()
    }

    @Test
    fun adCreateTestV1() {
        val (keyOut, keyIn) = with(app.processor1.processorConfig) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.config) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, AdCreateResponse::class.java)
                val expected = MkplAdStub.get()

                assertEquals(expected.title, response.ad?.title)
                assertEquals(expected.description, response.ad?.description)
            }
        }
    }

    @Test
    fun adCreateTestV2() {
        val (keyOut, keyIn) = with(app.processor2.processorConfig) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.config) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV2RequestSerialize(boltCreateV2).toByteArray())

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<AdCreateResponseV2>(responseJson)
                val expected = MkplAdStub.get()
                assertEquals(expected.title, response.ad?.title)
                assertEquals(expected.description, response.ad?.description)
            }
        }
    }

    private val boltCreateV1 = with(MkplAdStub.get()) {
        AdCreateRequest(
            ad = AdCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )
    }

    private val boltCreateV2 = with(MkplAdStub.get()) {
        AdCreateRequestV2(
            ad = AdCreateObjectV2(
                title = title,
                description = description
            ),
            debug = AdDebugV2(
                mode = AdRequestDebugModeV2.STUB,
                stub = AdRequestDebugStubsV2.SUCCESS
            )
        )
    }
}

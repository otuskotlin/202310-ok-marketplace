package ru.otus.otuskotlin.marketplace.app.rabbit

import com.rabbitmq.client.*
import kotlinx.coroutines.*
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import kotlin.coroutines.CoroutineContext

// абстрактный класс с boilerplate-кодом для связи с RMQ
/**
 * Абстрактный класс для процессоров-консьюмеров RabbitMQ
 * @property config - настройки подключения
 * @property processorConfig - настройки Rabbit exchange
 */
abstract class RabbitProcessorBase @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val config: RabbitConfig,
    val processorConfig: RabbitExchangeConfiguration,
    private val dispatcher: CoroutineContext = Dispatchers.IO.limitedParallelism(1) + Job(),
) {
    private var conn: Connection? = null
    private var chan: Channel? = null
    suspend fun process() = withContext(dispatcher) {
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            println("Creating new connection")
            conn = connection
            connection.createChannel().use { channel ->
                println("Creating new channel")
                chan = channel
                val deliveryCallback = channel.getDeliveryCallback()
                val cancelCallback = getCancelCallback()
                channel.describeAndListen(deliveryCallback, cancelCallback)
            }
        }
    }

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /**
     * Обработка ошибок
     */
    protected abstract fun Channel.onError(e: Throwable, delivery: Delivery)

    /**
     * Callback, который вызывается при доставке сообщения консьюмеру
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, delivery: Delivery ->
        runBlocking {
            kotlin.runCatching {
//                val routingKey: String = delivery.envelope.routingKey
//                val contentType: String = delivery.properties.contentType
                val deliveryTag: Long = delivery.envelope.deliveryTag
                processMessage(delivery)
                // Ручное подтверждение завершения обработки сообщения
                this@getDeliveryCallback.basicAck(deliveryTag, false)
            }.onFailure {
                onError(it, delivery)
            }
        }
    }

    /**
     * Callback, вызываемый при отмене консьюмера
     */
    private fun getCancelCallback() = CancelCallback {
        println("[$it] was cancelled")
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback
    ) {
        withContext(Dispatchers.IO) {
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            // Объявляем очередь (не сохраняется при перезагрузке сервера; неэксклюзивна - доступна другим соединениям;
            // не удаляется, если не используется)
            queueDeclare(processorConfig.queue, false, false, false, null)
            // связываем обменник с очередью по ключу (сообщения будут поступать в данную очередь с данного обменника при совпадении ключа)
            queueBind(processorConfig.queue, processorConfig.exchange, processorConfig.keyIn)
            // запуск консьюмера с автоотправкой подтверждение при получении сообщения
            basicConsume(processorConfig.queue, false, processorConfig.consumerTag, deliverCallback, cancelCallback)

            while (isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure { e ->
                    e.printStackTrace()
                }
            }

            println("Channel for [${processorConfig.consumerTag}] was closed.")
        }
    }

    fun close() {
        chan?.takeIf { it.isOpen }?.run {
            basicCancel(processorConfig.consumerTag)
            close()
            println("Close channel")
        }
        conn?.takeIf { it.isOpen }?.run {
            close()
            println("Close Rabbit connection")
        }
    }
}

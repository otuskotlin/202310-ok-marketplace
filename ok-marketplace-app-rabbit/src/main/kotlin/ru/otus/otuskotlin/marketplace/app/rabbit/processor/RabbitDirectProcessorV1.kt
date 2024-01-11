package ru.otus.otuskotlin.marketplace.app.rabbit.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.marketplace.app.rabbit.config.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.addError
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectProcessorV1(
    val appSettings: MkplAppSettings,
    processorConfig: RabbitExchangeConfiguration,
) : RabbitProcessorBase(appSettings.config, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery) {
        val context = MkplContext()
        context.apply {
            timeStart = Clock.System.now()
        }

        apiV1Mapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }
        val response = appSettings.processor.exec(context).run { context.toTransportAd() }
        apiV1Mapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MkplContext()
        e.printStackTrace()
        context.state = MkplState.FAILING
        context.addError(error = arrayOf(e.asMkplError()))
        val response = context.toTransportAd()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}

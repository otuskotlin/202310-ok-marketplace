package ru.otus.otuskotlin.marketplace.app.rabbit.controller

import kotlinx.coroutines.*
import ru.otus.otuskotlin.marketplace.app.rabbit.RabbitProcessorBase
import java.util.concurrent.atomic.AtomicBoolean

// запуск процессора
class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) : AutoCloseable {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val limitedParallelismContext = Dispatchers.IO.limitedParallelism(1)
    private val scope = CoroutineScope(Dispatchers.Default)
    private val runFlag = AtomicBoolean(true)

    fun start() {
        processors.forEach {
            scope.launch(
                limitedParallelismContext + CoroutineName("thread-${it.processorConfig.consumerTag}")
            ) {
                while (runFlag.get()) {
                    try {
                        println("Process...${it.processorConfig.consumerTag}")
                        it.process()
                    } catch (e: RuntimeException) {
                        // логируем, что-то делаем
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun stop() {
        runFlag.set(false)
        processors.forEach { it.close() }
    }

    override fun close() {
        stop()
        scope.cancel()
    }
}

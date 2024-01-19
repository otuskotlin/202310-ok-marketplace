package ru.otus.otuskotlin.marketplace.logging.socket

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class SocketLoggerTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun socketTest() = runTest(timeout = 30.seconds) {
        withContext(Dispatchers.Default) {
            // Prepare Server
            val selectorManager = SelectorManager(Dispatchers.IO)
            launch {
                aSocket(selectorManager).tcp().bind("127.0.0.1", 9002).use { serverSocket ->
                    serverSocket.accept().use { socket ->
                        flow<String> {
                            val receiveChannel = socket.openReadChannel()
                            while (true) {
                                receiveChannel.readUTF8Line(8 * 1024)?.let { emit(it) }
                            }
                        }.take(100).collect {
                            println("GOT: $it")
                        }
                    }
                }
            }
            // Prepare Logger
            val logger = mpLoggerSocket("test")
            // Wait for logger is ready
            while ((logger as? MpLoggerWrapperSocket)?.isReady?.value != true) {
                delay(1)
            }
            launch {
                repeat(100) {
                    println("logging: $it")
                    logger.info(
                        msg = "Test message $it",
                        marker = "TST",
                        data = object {
                            @Suppress("unused")
                            val str: String = "one"

                            @Suppress("unused")
                            val ival: Int = 2
                        }
                    )
                }
                println("Done!")
            }
        }
    }
}

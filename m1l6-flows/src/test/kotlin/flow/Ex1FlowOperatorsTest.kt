package ru.otus.otuskotlin.m1l6.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

class Ex1FlowOperatorsTest {

    @Test
    fun simple(): Unit = runBlocking {
        flowOf(1, 2, 3, 4)
            .onEach { println(it) }
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .collect { println("Result number $it") }
    }

    fun <T> Flow<T>.printThreadName(msg: String) =
        this.onEach { println("Msg = $msg, thread name = ${Thread.currentThread().name}") }

    @Test
    @OptIn(DelicateCoroutinesApi::class)
    fun coroutineContextChange(): Unit = runBlocking {
        newSingleThreadContext("Api-Thread").use { apiDispatcher ->
            newSingleThreadContext("Db-Thread").use { dbDispatcher ->
                flowOf(10, 20, 30)
                    .filter { it % 2 == 0 }
                    .map {
                        delay(2000)
                        it
                    }
                    .printThreadName("api call")
                    .flowOn(apiDispatcher)
                    .map { it + 1 }
                    .printThreadName("db call")
                    .flowOn(dbDispatcher)
                    .printThreadName("last operation")
                    .onEach { println("On each $it") }
                    .collect()
            }
        }
    }

    @Test
    fun startersStopers(): Unit = runBlocking {
        flow {
            while (true) {
                emit(1)
                delay(1000)
                emit(2)
                delay(1000)
                emit(3)
                delay(1000)
                throw RuntimeException("Custom error!")
            }
        }
            .onStart { println("On start") }
            .onCompletion { println(" On completion") }
            .catch { println("Catch: ${it.message}") }
            .onEach { println("On each: $it") }
            .collect { }
    }

    @Test
    fun buffering(): Unit = runBlocking {
        var sleepIndex = 1
        flow {
            while (sleepIndex < 3) {
                delay(500)
                emit(sleepIndex)
            }
        }
            .onEach { println("Send to flow: $it") }
            .buffer(3, BufferOverflow.DROP_LATEST)
            .onEach { println("Processing : $it") }
            .collect {
                println("Sleep")
                sleepIndex++
                delay(2_000)
            }
    }


    @Test
    fun customOperator(): Unit = runBlocking {
        fun <T> Flow<T>.zipWithNext(): Flow<Pair<T, T>> = flow {
            var prev: T? = null
            collect { el ->
                prev?.also { pr -> emit(pr to el) }
                prev = el
            }
        }

        flowOf(1, 2, 3, 4)
            .zipWithNext()
            .collect { println(it) }
    }

    @Test
    fun toListTermination(): Unit = runBlocking {
        val list = flow {
            emit(1)
            delay(100)
            emit(2)
            delay(100)
        }
            .onEach { println("$it") }
            .toList()

        println("List: $list")
    }

    @Test
    fun toList2(): Unit = runBlocking {
        val list = flow {
            var index = 0
            // If there is an infinite loop here, while (true)
            // then we will never output to the console
            //  println("List: $list")
            while (index < 10) {
                emit(index++)
                delay(100)
            }
        }
            .onEach { println("$it") }
            .toList()

        println("List: $list")
    }

    @OptIn(FlowPreview::class)
    @Test
    fun sampleDebounce() = runBlocking {
        val f = flow {
            repeat(20) {
                delay(100)
                emit(it)
                delay(400)
                emit("${it}a")
            }
        }

        println("SAMPLE")
        f.sample(200).collect {
            print(" $it")
        }
        println()
        println("DEBOUNCE")
        f.debounce(200).collect {
            print(" $it")
        }
    }

}



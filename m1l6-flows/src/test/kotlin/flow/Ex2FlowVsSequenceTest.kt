package ru.otus.otuskotlin.m1l6.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Test

class Ex2FlowVsSequenceTest {

    private fun simpleSequence(): Sequence<Int> = sequence {
        for (i in 1..5) {
//            delay(1000) // can't use it here
            Thread.sleep(1000)
            yield(i)
        }
    }

    private fun simpleFlow(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(1000)
            emit(i)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    // Thread.sleep блокирует корутину
    fun sequenceTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        simpleSequence().forEach { println(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flowTest(): Unit = runBlocking(Dispatchers.IO.limitedParallelism(1)) {
        launch {
            for (k in 1..5) {
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        simpleFlow()
            .collect { println(it) }

        println("Flow end")
    }
}

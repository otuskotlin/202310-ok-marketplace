package ru.otus.otuskotlin.m1l6.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

class Ex4FlowSharedTest {

    @Test
    fun shared(): Unit = runBlocking {
        val shFlow = MutableSharedFlow<String>()
        launch { shFlow.collect { println("XX $it") } }
        launch { shFlow.collect { println("YY $it") } }
        (1..10).forEach {
            delay(100)
            shFlow.emit("number $it")
        }
        coroutineContext.cancelChildren()
    }

    @Test
    fun collector(): Unit = runBlocking {
        val mshFlow = MutableSharedFlow<String>()
        val shFlow = mshFlow.asSharedFlow()
        val collector: FlowCollector<String> = mshFlow
        launch {
            mshFlow.collect {
                println("MUT $it")
            }
        }
        launch {
            shFlow.collect {
                println("IMMUT $it")
            }
        }
        delay(100)
        (1..20).forEach {
            collector.emit("zz: $it")
        }
        delay(1000)
        coroutineContext.cancelChildren()
    }

    @Test
    fun otherShared(): Unit = runBlocking {
        val coldFlow = flowOf(100, 101, 102, 103, 104, 105).onEach { println("Cold: $it") }

        launch { coldFlow.collect() }
        launch { coldFlow.collect() }

        val hotFlow = flowOf(200, 201, 202, 203, 204, 205)
            .onEach { println("Hot: $it") }
            .shareIn(this, SharingStarted.Lazily)

        launch { hotFlow.collect() }
        launch { hotFlow.collect() }

        delay(500)
        coroutineContext.cancelChildren()
    }

    @Test
    fun state(): Unit = runBlocking {
        val mshState = MutableStateFlow("state1")
        val shState = mshState.asStateFlow()
        val collector: FlowCollector<String> = mshState
        launch { mshState.collect { println("MUT $it") } }
        launch { shState.collect { println("IMMUT $it") } }
        launch {
            (1..20).forEach {
                delay(20)
                collector.emit("zz: $it")
            }
        }
        delay(100)
        println("FINAL STATE: ${shState.value}")
        coroutineContext.cancelChildren()
    }
}

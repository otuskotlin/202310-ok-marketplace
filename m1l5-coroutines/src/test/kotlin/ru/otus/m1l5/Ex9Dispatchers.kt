package ru.otus.m1l5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Ex10Dispatchers {
    fun CoroutineScope.createCoro() {
        repeat(30) {
            launch {
                println("coroutine $it, start")
                Thread.sleep(500)
                println("coroutine $it, end")
            }
        }
    }

    @Test
    fun default() {
        CoroutineScope(Job()).createCoro()
        Thread.sleep(2000)
    }

    @Test
    fun io() {
        CoroutineScope(Job() + Dispatchers.IO).createCoro()
        Thread.sleep(2000)
    }

    @Test
    fun custom() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        CoroutineScope(Job() + dispatcher).createCoro()
        Thread.sleep(6000)
    }

    @Test
    fun unconfined() {
        val scope = CoroutineScope(Dispatchers.Default)
        //val scope = CoroutineScope(Dispatchers.Unconfined)

        scope.launch() {
            println("start coroutine")
            suspendCoroutine {
                println("suspend function, start")
                thread {
                    println("suspend function, background work")
                    Thread.sleep(1000)
                    it.resume("Data!")
                }
            }
            println("end coroutine")
        }

        Thread.sleep(2000)
    }

}

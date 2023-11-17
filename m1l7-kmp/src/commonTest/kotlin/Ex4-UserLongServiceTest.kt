package ru.otus.otuskotlin.m1l7

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

class UserLongServiceTest {

    @Test
    fun longCoroutines() = runTest {
        val timeStart = Clock.System.now()
        measureTime {
            val user = User("1", "Ivan", 24)
            val service = UserLongService()
            val res = service.serve(user)
            assertEquals(res.second, user)
            assertTrue {
                res.first == "JS"
                        || res.first == "JVM"
                        || res.first == "Native"
            }
        }.also { println("GENERAL TIME: $it; REAL TIME: ${Clock.System.now() - timeStart}") }
    }

    @Test
    fun realTime() = runTest(timeout = 10.seconds) {
        withContext(Dispatchers.Default) {
            val timeStart = Clock.System.now()
            measureTime {
                val user = User("1", "Ivan", 24)
                val service = UserLongService()
                val res = service.serve(user)
                assertEquals(res.second, user)
                assertTrue {
                    res.first == "JS"
                            || res.first == "JVM"
                            || res.first == "Native"
                }
            }.also { println("GENERAL TIME: $it; REAL TIME: ${Clock.System.now() - timeStart}") }
        }
    }
}

package ru.otus.otuskotlin.m1l4

import org.junit.Test
import kotlin.test.assertEquals

class SimpleTestCase {
    @Test
    fun `minimal test`() {
        val s = sout {
            1 + 123
        }

        assertEquals("string 124", s)
    }

    @Test
    fun `sout with prefix`() {
        soutWithTimestamp {
            "${time()}: my line."
        }
    }

    @Test
    fun `dsl functions`() {
        val (key, value) = Pair("key", "value")
        assertEquals(key, "key")
        assertEquals(value, "value")

        val pairNew = "key" to "value"
        assertEquals(pairNew.first, "key")
        assertEquals(pairNew.second, "value")

        val myTimeOld = "12".time("30")
        assertEquals(myTimeOld, "12:30")

        val myTime = "12" time "30"
        assertEquals(myTime, "12:30")
    }

    private fun sout(block: () -> Int?): String {
        val result = block()
        println(result)
        return "string $result"
    }

    class MyContext {
        fun time() = System.currentTimeMillis()
//    // Same as:
//    fun time(): Long {
//        return System.currentTimeMillis()
//    }
    }

    private fun soutWithTimestamp(block: MyContext.() -> Any?) {
        val context = MyContext()
        val result = block(context)
        println(result)
    }

    private infix fun String.time(value: String): String {
        return "$this:$value"
    }
}

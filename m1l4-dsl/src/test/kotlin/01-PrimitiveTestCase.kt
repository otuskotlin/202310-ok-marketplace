package ru.otus.otuskotlin.m1l4

import org.junit.Test
import kotlin.test.assertEquals

class PrimitiveTestCase {
    @Test
    fun builderLessTest() {
        class SomeTest(
            val x: Int = 0,
            val s: String = "string $x",
        )
        val inst = SomeTest(5)
        assertEquals("stirng 5", inst.s)
    }
}

@file:Suppress("unused")
package ru.otus.otuskotlin.oop

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

enum class SimpleEnum {
    LOW,
    HIGH
}

enum class EnumWithData(val level: Int, val description: String) {
    LOW(10, "low level"),
    HIGH(20, "high level")
}


enum class MyEnum : Iterable<MyEnum> {
    FOO {
        override fun doSmth() {
            println("do foo")
        }
    },

    BAR {
        override fun doSmth() {
            println("do bar")
        }
    };

    abstract fun doSmth()

    override fun iterator(): Iterator<MyEnum> = listOf(FOO, BAR).iterator()
}

class EnumsTest {
    @Test
    fun enum() {
        var e = SimpleEnum.LOW
        println(e)

        e = SimpleEnum.valueOf("HIGH")
        println(e)
        println(e.ordinal)
        assertEquals(e.ordinal, 1)

        println(SimpleEnum.entries.joinToString())
    }

    @Test
    fun interfacedEnums() {
        assertEquals(listOf(MyEnum.FOO, MyEnum.BAR), MyEnum.BAR.iterator().asSequence().toList())
        assertEquals(listOf(MyEnum.FOO, MyEnum.BAR), MyEnum.FOO.iterator().asSequence().toList())
    }

    @Test
    fun enumFailures() {
        assertFails {
            // Здесь будет исключение в рантайме
            SimpleEnum.valueOf("high")
        }

        val res = runCatching { SimpleEnum.valueOf("high") }
            .getOrDefault(SimpleEnum.HIGH)

        assertEquals(SimpleEnum.HIGH, res)
    }
}

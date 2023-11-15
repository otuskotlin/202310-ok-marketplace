package ru.otus.otuskotlin.m1l6.sequence

import org.junit.jupiter.api.Test

/**
 * Упростить примеры, убрать лишнее
 */

enum class Color {
    YELLOW,
    GREEN,
    BLUE,
    RED,
    VIOLET,

}

enum class Shape {
    SQUARE,
    CIRCLE,
    TRIANGLE,
    RHOMBUS

}

data class Figure(
    val color: Color,
    val shape: Shape
)

fun processAsList(
    words: List<String>,
    wordLength: Int, take: Int,
    printOperation: Boolean
) {
    println("Processing list")
    var counter = 0

    val lengthsList = words
        .filter {
            counter++
            if (printOperation) println("filter: $it")

            it.length > wordLength
        }
        .map {
            counter++
            if (printOperation) println("length: ${it.length}")

            it.length
        }
        .take(take)

    println("Lengths of first $take words longer than $wordLength chars:")
    println(lengthsList)

    println("List counter: $counter")
    println()
}

fun processAsSeq(
    words: List<String>,
    wordLength: Int,
    take: Int,
    printOperation: Boolean
) {
    println("Processing sequence")
    var counter = 0

    //convert the List to a Sequence
    val wordsSequence = words.asSequence()

    val lengthsSequence = wordsSequence
        .filter {
            counter++
            if (printOperation) println("filter: $it")

            it.length > wordLength
        }
        .map {
            counter++
            if (printOperation) println("length: ${it.length}")

            it.length
        }
        .take(take)
        .toList()

    println("Lengths of first $take words longer than $wordLength chars")
    println(lengthsSequence)

    println("Sequence counter: $counter")
    println()
}

class SequenceTest {

    companion object {
        private val testFigureList = listOf(
            Figure(Color.GREEN, Shape.CIRCLE),
            Figure(Color.VIOLET, Shape.SQUARE),
            Figure(Color.BLUE, Shape.RHOMBUS),
            Figure(Color.RED, Shape.TRIANGLE)
        )
    }

    /**
     * В коллекции выполняется перекраска всех элементов списка
     */
    @Test
    fun collection() {
        var counter = 0
        val figure = testFigureList
            .map {
                counter++
                println("Change color")
                it.copy(color = Color.YELLOW)
            }
            .first {
                counter++
                println("Filter by shape")
                it.shape == Shape.SQUARE
            }
        println("Figure: $figure")
        println("Counter: $counter")
    }

    /**
     * В последовательности цепочка обработок такая же как в коллекции, но обработки выполнены только необходимые
     */
    @Test
    fun sequence() {
        var counter = 0
        val figure = testFigureList.asSequence()
            .map {
                counter++
                println("Change color")
                it.copy(color = Color.YELLOW)
            }
            .first {
                counter++
                println("Filter by shape")
                it.shape == Shape.SQUARE
            }
        println("Figure: $figure")
        println("Counter: $counter")
    }

    /**
     * Самостоятельно сравните код в processAsList и processAsSeq. При запуске вы увидите, что число операций в
     * последовательности меньше
     */
    @Test
    fun smallSequence() {
        val words = "The quick brown fox jumps over the lazy dog".split(" ")

        val wordLength = 3
        val take = 3
        val printOperation = false

        processAsList(words, wordLength, take, printOperation)
        processAsSeq(words, wordLength, take, printOperation)
    }

    /**
     * Как выше, просто размер последовательности больше
     */
    @Test
    fun bigSequence() {
        val words = ("So, the sequences let you avoid building results of intermediate steps, " +
                "therefore improving the performance of the whole collection processing chain. " +
                "However, the lazy nature of sequences adds some overhead which may be significant " +
                "when processing smaller collections or doing simpler computations. Hence, you should consider both " +
                "Sequence and Iterable and decide which one is better for your case.")
            .split(" ")

        val wordLength = 3
        val take = 10

        val printOperation = false

        processAsList(words, wordLength, take, printOperation)
        processAsSeq(words, wordLength, take, printOperation)
    }


    @Test
    fun collectionIsNotLazy() {
        var counter = 0
        val list = listOf(1, 2, 3, 4)
            .map {
                counter++
                it * it
            }
            .take(2)
        println("List: $list")
        println("Counter: $counter")
    }

    @Test
    fun sequenceIsLazy() {
        var counter = 0
        val sequence = sequenceOf(1, 2, 3, 4)
            .map {
                counter++
                it * it
            }
            .take(2)

        println("Sequence: $sequence")
        println("Counter: $counter")

        val result = sequence
            .toList()

        println("Result: $result")
        println("Counter: $counter")
    }

    /**
     * Здесь испльзуется блокирующая задержка на 3 секунды. Вы должны увидеть, недостаток, который покрывается в
     * корутинах и в Flow
     */
    @Test
    fun blockingCall() {
        val sequence = sequenceOf(1, 2, 3)
            .map {
                println("Make blocking call to API")
                Thread.sleep(3000)
                it + 1
            }
            .toList()
        println("Sequence: $sequence")
    }

    /**
     * Демонстрация холодного поведения. Последовательность вызывается оба раза с нуля.
     */
    @Test
    fun coldFeature() {
        val seq = sequence {
            var x = 0
            for (i in (1..15)) {
                x += i
                yield(x)
            }
        }

        val s1 = seq.map { it } // не запущено
        val s2 = seq.map { it * 2 } // не работает
        // Здесь seq вызвалась оба раза
        println("S1: ${s1.toList()}") // терминальный оператор здесь
        println("S2: ${s2.toList()}")
    }

}

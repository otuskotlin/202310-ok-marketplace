package ru.otus.otuskotlin.m1l6.sequence

import kotlin.test.Test


class SequenceTest {

    /**
     * В коллекции операторы преобразования выполняются преобразования сверху вниз последовательно для всей коллекции
     */
    @Test
    fun collectionIsNotLazy() {
        listOf(1, 2, 3, 4)
            .map {
                println("map for $it -> ${it*it}")
                it to it * it
            }
            .first {
                println("first for ${it.first}")
                it.first == 3
            }
    }

    /**
     * В последовательности цепочка обработок такая же как в коллекции, но обработки выполнены только необходимые
     */
    @Test
    fun sequenceIsLazy() {
        listOf(1, 2, 3, 4).asSequence()
            .map {
                println("map for $it -> ${it*it}")
                it to it * it
            }
            .first {
                println("first for ${it.first}")
                it.first == 3
            }
    }


    /**
     * Здесь используется блокирующая задержка на 3 секунды. Никакой параллельной обработки последовательности не дают.
     * Вы должны увидеть недостаток, который покрывается в корутинах и в Flow.
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
        // Это просто конфигурирование последовательности, ничего не вычисляется в этом месте
        val seq = sequence {
            var x = 0
            for (i in (1..15)) {
                x += i
                yield(x)
            }
        }

        val s1 = seq.map { it } // не запущено выполнение, результата еще нет
        val s2 = seq.map { it * 2 } // тоже

        // Здесь seq вызывается оба раза и выполняется с нуля
        println("S1: ${s1.toList()}") // терминальный оператор здесь,
        println("S2: ${s2.toList()}") // т.е. только здесь запускается выполнение и получение результата
    }

}

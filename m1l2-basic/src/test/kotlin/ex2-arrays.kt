import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArraysTest {
    @Test
    fun arrays() {
        val arrayOfInt = arrayOf(1, 2, 3)
        println(arrayOfInt)
        println(arrayOfInt.contentToString())

        val emptyArray = emptyArray<Int>() // arrayOf()

        val arrayCalculated = Array(5) { i -> (i * i) }
        println(arrayCalculated.contentToString())

        val intArray = intArrayOf(1, 2, 3) // int[]

        val element = arrayOfInt[2] // 3
        val elementByFunction = arrayOfInt.get(2) // 3
        assertEquals(element, elementByFunction)

    }
}
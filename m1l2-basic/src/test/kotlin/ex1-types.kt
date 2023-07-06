import org.junit.Test
import kotlin.test.assertTrue

inline fun <reified T> assertType(v: Any) {
    assertTrue("${v::class.java} != ${T::class.java}") { v::class == T::class }
}

class TypesTest {
    @Test
    fun declare() {
        val b: Byte = 1
        assertType<Byte>(b)
        assertType<Int>(1)

        val b2: Byte = 1 + 2 // WARNING
        assertType<Byte>(b2)
        assertType<Int>(1 + 2)

        val b3 = 2.toByte()
        assertType<Byte>(b3)

        //val ub: UByte = 1 // ERROR
        val ub2: UByte = 1U
        assertType<UByte>(ub2)

        val l =  1L
        assertType<Long>(l)

        val f = 1.2f
        assertType<Float>(f)
        // val f2 : Float = 1.2 // ERROR

        val d = 1.2
        assertType<Double>(d)
    }

    @Test
    fun conversions() {
        val a = 1
        //val b: Long = a // ERROR
        val b: Long = a.toLong()

        val f: Float = 1.0f

        val d = a * 1.0
        assertType<Double>(d)

        val l = a + 2L
        assertType<Long>(l)

        // val ui = a + 2U // ERROR
        val ui2 = a.toUInt() + 2U
        assertType<UInt>(ui2)
    }
}
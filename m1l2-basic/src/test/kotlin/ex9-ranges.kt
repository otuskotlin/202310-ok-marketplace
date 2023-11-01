import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RangesTest {

    @Test
    fun inclusive() {
        val list = (1..5).toList()
        assertContains(list, 1)
        assertContains(list, 5)
    }

    @Test
    fun exclusive() {
        val list = (1 until 5).toList()
        assertContains(list, 1)
        assertFalse(list.contains(5))
    }

    @Test
    fun countDown() {
        val list = (5 downTo 1).toList()
        assertEquals(5, list.first())
        assertEquals(1, list.last())
    }

    @Test
    fun step() {
        val list = (1 .. 5 step 2).toList()
        assertTrue(list.contains(1))
        assertFalse(list.contains(2))
    }
}

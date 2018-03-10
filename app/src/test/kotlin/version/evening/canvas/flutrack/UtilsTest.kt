package version.evening.canvas.flutrack

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun testMillisToReadableDate() {
        val millis = "1519843095"
        val expected = "Feb 28, 2018"

        val actual = millis.millisToReadableDate()

        assertEquals(expected, actual)
    }
}

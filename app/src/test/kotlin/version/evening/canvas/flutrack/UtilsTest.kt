package version.evening.canvas.flutrack

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar

class UtilsTest {
    @Test
    fun testMillisToReadableDate() {
        val millis = "1519843095"
        val expected = "Feb 28, 2018"

        val actual = millis.millisToReadableDate()

        assertEquals(expected, actual)
    }

    @Test
    fun testDefaultDateFormat() {
        val calendar = Calendar.getInstance()

        val time = calendar.time
        val result = calendar.defaultDateFormat()

        val formatter = SimpleDateFormat.getInstance()
        val expected = formatter.format(time)

        assertEquals(expected, result)
    }

    @Test
    fun testToBooleanExtra() {
        val falseValues = listOf("", "false", "0", "FALSE")
        val trueValues = listOf("sfjdl", "24", "T232rue")

        falseValues.forEach { assertFalse(it.toBooleanExtra()) }
        trueValues.forEach { assertTrue(it.toBooleanExtra()) }
    }

    @Test
    fun testDoOnFirst() {
        val data = listOf(3, 2, 1)
        var timesCalled = 0

        Observable.fromIterable(data).doOnFirst { ++timesCalled }.subscribe()

        assertEquals(1, timesCalled)
    }
}

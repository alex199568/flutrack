package version.evening.canvas.flutrack

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InputModifierTest {
    private lateinit var inputModifier: InputModifier

    @Before
    fun setup() {
        inputModifier = InputModifier()
    }

    @Test
    fun testInputModifier() {
        val input = "Some Input"
        val expectedOutput = "SOME INPUT"

        val result = inputModifier.modify(input)

        assertEquals(result, expectedOutput)
    }
}

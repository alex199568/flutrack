package version.evening.canvas.flutrack

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainScreenTest {
    private lateinit var inputModifier: InputModifier

    @Rule @JvmField
    val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setup() {
        inputModifier = InputModifier()
    }

    @Test
    fun testProcessButton() {
        val input = "Some Input"
        val output = inputModifier.modify(input)

        onView(withId(R.id.input_field)).perform(typeText(input))
        onView(withId(R.id.process_button)).perform(click())

        onView(withId(R.id.output_field)).check(matches(withText(output)))
    }
}

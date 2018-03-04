package version.evening.canvas.flutrack

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainScreenTest {
    @Rule @JvmField
    val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testStartedWithMapFragment() {
        onView(withId(R.id.fragmentLabel)).check(matches(withText("Map")))
    }

    @Test
    fun testPressMapButton() {
        onView(withId(R.id.map)).perform(click())

        onView(withId(R.id.fragmentLabel)).check(matches(withText("Map")))
    }

    @Test
    fun testPressDashboardButton() {
        onView(withId(R.id.dashboard)).perform(click())

        onView(withId(R.id.fragmentLabel)).check(matches(withText("Dashboard")))
    }

    @Test
    fun testPressAboutButton() {
        onView(withId(R.id.about)).perform(click())

        onView(withId(R.id.fragmentLabel)).check(matches(withText("About")))
    }
}

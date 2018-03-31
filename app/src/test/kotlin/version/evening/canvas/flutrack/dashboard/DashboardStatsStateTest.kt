package version.evening.canvas.flutrack.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test
import version.evening.canvas.flutrack.data.FluTweet

class DashboardStatsStateTest {
    @Test
    fun testCase1() {
        val state = DashboardStatsState()
        val tweet = FluTweet(tweetText = "flu")

        state.processTweet(tweet)

        val result = state.asDashboardStats()

        assertEquals(1, result.numberOfTweets)
        assertEquals(1, result.totalSymptoms)
        assertEquals("Flu", result.mostFrequentSymptom)
        assertEquals(1, result.mostFrequentSymptomNumber)
        assertEquals(100, result.mostFrequentSymptomPercentage)
    }
}

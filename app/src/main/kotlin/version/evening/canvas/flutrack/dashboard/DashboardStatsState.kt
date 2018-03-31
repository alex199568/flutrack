package version.evening.canvas.flutrack.dashboard

import version.evening.canvas.flutrack.data.FluTweet
import kotlin.math.roundToInt

private const val PERCENT = 100.0

data class DashboardStatsState(
        private var totalTweets: Int = 0,
        private var totalSymptoms: Int = 0,
        private val counter: MutableMap<Symptom, Int> = mutableMapOf()
) {
    init {
        Symptom.values().forEach { counter[it] = 0 }
    }

    fun asDashboardStats(): DashboardStats {
        val mostFrequent = counter.maxBy { it.value }
        val mostFrequentSymptom = mostFrequent?.key
                ?: throw IllegalStateException("no most frequent symptom")
        val mostFrequentNumber = mostFrequent.value

        return DashboardStats(
                totalTweets,
                mostFrequentSymptom.symptom.capitalize(),
                mostFrequentNumber,
                (mostFrequentNumber.toDouble() / totalSymptoms.toDouble() * PERCENT).roundToInt(),
                totalSymptoms
        )
    }

    fun processTweet(tweet: FluTweet) {
        ++totalTweets
        Symptom.values().forEach {
            if (tweet.tweetText?.contains(it.symptom, true) == true) {
                counter[it] = counter[it]?.plus(1) ?: 0
                ++totalSymptoms
            }
        }
    }
}


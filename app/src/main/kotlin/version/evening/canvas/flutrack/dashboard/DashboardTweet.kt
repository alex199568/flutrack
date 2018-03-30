package version.evening.canvas.flutrack.dashboard

import version.evening.canvas.flutrack.data.FluTweet

data class DashboardTweet(
        val text: String
) {
    constructor(fluTweet: FluTweet) : this(
            text = fluTweet.tweetText ?: ""
    )
}

package version.evening.canvas.flutrack.data

import com.google.gson.annotations.SerializedName

data class FluTweet(
        @SerializedName("user_name") val userName: String? = null,
        @SerializedName("tweet_text") val tweetText: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        @SerializedName("tweet_date") val tweetDate: String? = null,
        val aggravation: String? = null
)

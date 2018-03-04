package version.evening.canvas.flutrack.api

import com.google.gson.annotations.SerializedName

data class FluTweetDataClass(
        @SerializedName("user_name") val userName: String,
        @SerializedName("tweet_text") val tweetText: String,
        val latitude: Double,
        val longitude: Double,
        @SerializedName("tweet_date") val tweetDate: String,
        val aggravation: String
)

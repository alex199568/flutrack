package version.evening.canvas.flutrack.map

import com.google.android.gms.maps.model.LatLng
import version.evening.canvas.flutrack.toBooleanExtra
import version.evening.canvas.flutrack.api.FluTweet
import version.evening.canvas.flutrack.defaultDateFormat
import version.evening.canvas.flutrack.millisToReadableDate
import java.util.Calendar

data class MapTweet (
        val userName: String,
        val tweetText: String,
        private val latitude: Double,
        private val longitude: Double,
        val tweetDate: String,
        val aggravation: Boolean
) {
    val latLng: LatLng
    get() = LatLng(latitude, longitude)

    constructor(fluTweet: FluTweet) : this(
            userName = fluTweet.userName ?: "Unknown",
            tweetText = fluTweet.tweetText ?: "NO_TEXT",
            latitude = fluTweet.latitude ?: 0.0,
            longitude = fluTweet.longitude ?: 0.0,
            tweetDate = fluTweet.tweetDate?.millisToReadableDate() ?: Calendar.getInstance().defaultDateFormat(),
            aggravation = fluTweet.aggravation?.toBooleanExtra() ?: false
    )
}

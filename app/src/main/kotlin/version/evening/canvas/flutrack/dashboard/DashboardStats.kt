package version.evening.canvas.flutrack.dashboard

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
data class DashboardStats(
        val numberOfTweets: Int = 0,
        val mostFrequentSymptom: String = "",
        val mostFrequentSymptomNumber: Int = 0,
        val mostFrequentSymptomPercentage: Int = 0,
        val totalSymptoms: Int = 0
) : Parcelable

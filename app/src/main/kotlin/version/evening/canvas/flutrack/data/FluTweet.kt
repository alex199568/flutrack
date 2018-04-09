package version.evening.canvas.flutrack.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo
import com.google.gson.annotations.SerializedName

@Entity(tableName = "flutweet")
data class FluTweet(
        @PrimaryKey(autoGenerate = true) var id: Int = 0,
        @ColumnInfo(name = "user_name") @SerializedName("user_name") var userName: String? = null,
        @ColumnInfo(name = "tweet_text") @SerializedName("tweet_text") var tweetText: String? = null,
        @ColumnInfo(name = "latitude") var latitude: Double? = null,
        @ColumnInfo(name = "longitude") var longitude: Double? = null,
        @ColumnInfo(name = "tweet_date") @SerializedName("tweet_date") var tweetDate: String? = null,
        @ColumnInfo(name = "aggravation") var aggravation: String? = null
)

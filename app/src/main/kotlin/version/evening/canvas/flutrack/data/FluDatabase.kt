package version.evening.canvas.flutrack.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [FluTweet::class], version = 1)
abstract class FluDatabase : RoomDatabase() {
    abstract fun fluTweetDao(): FluTweetDao
}

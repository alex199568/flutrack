package version.evening.canvas.flutrack.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.FluTweetDao
import javax.inject.Inject

class MapViewModel(
        fluTweetDao: FluTweetDao
) : ViewModel() {
    val data: LiveData<List<FluTweet>> = fluTweetDao.getAll()

    class Factory @Inject constructor(
            private val fluTweetDao: FluTweetDao
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapViewModel(fluTweetDao) as T
        }
    }
}

package version.evening.canvas.flutrack.dashboard

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import version.evening.canvas.flutrack.data.FluTweetDao
import javax.inject.Inject

class DashboardViewModel(
        fluTweetDao: FluTweetDao
) : ViewModel() {
    val statsData: LiveData<DashboardStats?> = Transformations.map(fluTweetDao.getAll()) {
        if (it.isEmpty()) {
            null
        } else {
            it.fold(DashboardStatsState(), { state, fluTweet -> state.processTweet(fluTweet) }).asDashboardStats()
        }
    }

    class Factory @Inject constructor(
            private val fluTweetDao: FluTweetDao
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DashboardViewModel(fluTweetDao) as T
        }
    }
}

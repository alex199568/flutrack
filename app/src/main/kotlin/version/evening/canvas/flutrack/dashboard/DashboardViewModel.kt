package version.evening.canvas.flutrack.dashboard

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.Observable
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import javax.inject.Inject

class DashboardViewModel(
        storage: MemoryFlutweetsStorage,
        schedulers: SchedulersWrapper
) : ViewModel() {
    val statsData = MutableLiveData<DashboardStats>()

    init {
        storage.data.observeForever {
            it?.let {
                Observable.fromIterable(it)
                        .collect({ DashboardStatsState() }, { state, tweet -> state.processTweet(tweet) })
                        .map { it.asDashboardStats() }
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.android())
                        .subscribe { stats -> statsData.postValue(stats) }
            }
        }
    }

    class Factory @Inject constructor(
            private val storage: MemoryFlutweetsStorage,
            private val schedulers: SchedulersWrapper
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DashboardViewModel(storage, schedulers) as T
        }
    }
}

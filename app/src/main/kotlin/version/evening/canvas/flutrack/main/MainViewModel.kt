package version.evening.canvas.flutrack.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.FluTweetDao
import javax.inject.Inject

class MainViewModel(
        private val flutrackAll: FlutrackAll,
        private val fluTweetDao: FluTweetDao,
        private val schedulersWrapper: SchedulersWrapper
) : ViewModel() {
    private var requestInProgress = false

    val onError = MutableLiveData<Unit>()

    fun requestFlutweets() {
        if (requestInProgress) {
            return
        }
        requestInProgress = true
        flutrackAll
                .results()
                .subscribeOn(schedulersWrapper.io())
                .observeOn(schedulersWrapper.io())
                .doOnTerminate { requestInProgress = false }
                .subscribe({
                    fluTweetDao.apply {
                        deleteAll()
                        save(it)
                    }
                }, { onError.postValue(Unit) })
    }

    class Factory @Inject constructor(
            private val flutrackAll: FlutrackAll,
            private val fluTweetDao: FluTweetDao,
            private val schedulersWrapper: SchedulersWrapper
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(flutrackAll, fluTweetDao, schedulersWrapper) as T
        }
    }
}

package version.evening.canvas.flutrack.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private var requestInProgress = false

    val onError = MutableLiveData<Unit>()

    @Inject
    lateinit var flutrackAll: FlutrackAll
    @Inject
    lateinit var inMemoryStorage: MemoryFlutweetsStorage
    @Inject
    lateinit var schedulersWrapper: SchedulersWrapper

    fun requestFlutweets() {
        if (requestInProgress) {
            return
        }
        requestInProgress = true
        flutrackAll
                .results()
                .subscribeOn(schedulersWrapper.io())
                .doOnTerminate { requestInProgress = false }
                .subscribe({ inMemoryStorage.rewrite(it) }, { onError.postValue(Unit) })
    }
}

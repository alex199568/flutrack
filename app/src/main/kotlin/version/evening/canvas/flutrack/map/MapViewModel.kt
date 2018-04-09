package version.evening.canvas.flutrack.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import javax.inject.Inject

class MapViewModel(
        memoryStorage: MemoryFlutweetsStorage
) : ViewModel() {
    val data: LiveData<List<FluTweet>> = memoryStorage.data

    class Factory @Inject constructor(
            private val memoryStorage: MemoryFlutweetsStorage
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapViewModel(memoryStorage) as T
        }
    }
}

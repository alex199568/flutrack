package version.evening.canvas.flutrack.map

import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

@Module
class MapModule(private val view: MapContract.View) {
    @MapScope
    @Provides
    fun providePresenter(storage: MemoryFlutweetsStorage, schedulers: SchedulersWrapper): MapContract.Presenter {
        return MapPresenter(storage, schedulers, view)
    }
}

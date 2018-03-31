package version.evening.canvas.flutrack.main

import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

@Module
class MainModule(
        private val view: MainContract.View
) {
    @MainScope
    @Provides
    fun providePresenter(
            flutrackAll: FlutrackAll,
            storage: MemoryFlutweetsStorage,
            schedulers: SchedulersWrapper
    ): MainContract.Presenter {
        return MainPresenter(flutrackAll, storage, schedulers, view)
    }
}

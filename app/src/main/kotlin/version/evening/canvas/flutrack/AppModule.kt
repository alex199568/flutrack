package version.evening.canvas.flutrack

import android.content.Context
import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import version.evening.canvas.flutrack.main.MainViewModel
import version.evening.canvas.flutrack.map.MapViewModel

@Module
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context = context

    @AppScope
    @Provides
    fun provideSchedulers(): SchedulersWrapper = SchedulersWrapper()

    @AppScope
    @Provides
    fun provideMainViewModelFactory(
            flutrackAll: FlutrackAll,
            memoryFlutweetsStorage: MemoryFlutweetsStorage,
            schedulersWrapper: SchedulersWrapper
    ): MainViewModel.Factory = MainViewModel.Factory(flutrackAll, memoryFlutweetsStorage, schedulersWrapper)

    @AppScope
    @Provides
    fun provideMapViewModelFactory(
            memoryFlutweetsStorage: MemoryFlutweetsStorage
    ): MapViewModel.Factory = MapViewModel.Factory(memoryFlutweetsStorage)
}

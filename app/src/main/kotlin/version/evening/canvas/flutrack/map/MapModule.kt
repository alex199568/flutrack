package version.evening.canvas.flutrack.map

import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.api.FlutrackAll

@Module
class MapModule(private val flutrackAll: FlutrackAll, private val schedulersWrapper: SchedulersWrapper) {
    @MapScope
    @Provides
    fun provideMapModel(): MapModel = MapModel(flutrackAll)

    @MapScope
    @Provides
    fun provideMapViewModel(model: MapModel): MapViewModel = MapViewModel(model, schedulersWrapper)
}

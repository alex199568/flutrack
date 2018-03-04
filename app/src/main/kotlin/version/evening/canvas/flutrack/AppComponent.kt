package version.evening.canvas.flutrack

import dagger.Component
import version.evening.canvas.flutrack.api.FlutrackAll
import version.evening.canvas.flutrack.api.FlutrackModule

@AppScope
@Component(modules = [(FlutrackModule::class), (AppModule::class)])
interface AppComponent {
    fun flutrackAll(): FlutrackAll
}

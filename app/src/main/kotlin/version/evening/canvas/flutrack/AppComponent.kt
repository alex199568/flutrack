package version.evening.canvas.flutrack

import dagger.Component
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.FlutrackModule
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

@AppScope
@Component(modules = [FlutrackModule::class, AppModule::class])
interface AppComponent {
    fun flutrackAll(): FlutrackAll
    fun memoryFlutweetsStorage(): MemoryFlutweetsStorage
    fun schedulers(): SchedulersWrapper
}

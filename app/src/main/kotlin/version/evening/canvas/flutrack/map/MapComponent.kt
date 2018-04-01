package version.evening.canvas.flutrack.map

import dagger.Component
import version.evening.canvas.flutrack.AppComponent

@MapScope
@Component(modules = [MapModule::class], dependencies = [AppComponent::class])
interface MapComponent {
    fun inject(fragment: MapFragment)
}

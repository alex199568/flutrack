package version.evening.canvas.flutrack.map

import dagger.Component

@MapScope
@Component(modules = [(MapModule::class)])
interface MapComponent {
    fun inject(fragment: MapFragment)
}

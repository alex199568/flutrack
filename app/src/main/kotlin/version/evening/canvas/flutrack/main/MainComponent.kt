package version.evening.canvas.flutrack.main

import dagger.Component
import version.evening.canvas.flutrack.AppComponent

@MainScope
@Component(modules = [MainModule::class], dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}

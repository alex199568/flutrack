package version.evening.canvas.flutrack.dashboard

import dagger.Component
import version.evening.canvas.flutrack.AppComponent

@DashboardScope
@Component(modules = [DashboardModule::class], dependencies = [AppComponent::class])
interface DashboardComponent {
    fun inject(fragment: DashboardFragment)
}

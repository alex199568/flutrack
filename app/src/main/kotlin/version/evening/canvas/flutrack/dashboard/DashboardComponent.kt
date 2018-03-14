package version.evening.canvas.flutrack.dashboard

import dagger.Component

@DashboardScope
@Component(modules = [(DashboardModule::class)])
interface DashboardComponent {
    fun inject(fragment: DashboardFragment)
}

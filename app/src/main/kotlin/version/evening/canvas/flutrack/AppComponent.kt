package version.evening.canvas.flutrack

import dagger.Component
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import version.evening.canvas.flutrack.data.FluTweetDao
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.FlutrackModule
import version.evening.canvas.flutrack.main.MainActivity
import version.evening.canvas.flutrack.map.MapFragment

@AppScope
@Component(modules = [FlutrackModule::class, AppModule::class])
interface AppComponent {
    fun flutrackAll(): FlutrackAll
    fun fluTweetDao(): FluTweetDao
    fun schedulers(): SchedulersWrapper

    fun inject(activity: MainActivity)
    fun inject(mapFragment: MapFragment)
    fun inject(dashboardFragment: DashboardFragment)
}

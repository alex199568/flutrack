package version.evening.canvas.flutrack.dashboard

import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll

@Module
class DashboardModule(private val flutrackAll: FlutrackAll, private val schedulersWrapper: SchedulersWrapper) {
    @DashboardScope
    @Provides
    fun provideDashboardModel(): DashboardModel = DashboardModel(flutrackAll)

    @DashboardScope
    @Provides
    fun provideDashboardViewModel(model: DashboardModel): DashboardViewModel =
            DashboardViewModel(model, schedulersWrapper)
}

package version.evening.canvas.flutrack.dashboard

import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

@Module
class DashboardModule(private val view: DashboardContract.View) {
    @DashboardScope
    @Provides
    fun providePresenter(
            storage: MemoryFlutweetsStorage,
            schedulersWrapper: SchedulersWrapper
    ): DashboardContract.Presenter {
        return DashboardPresenter(storage, schedulersWrapper, view)
    }
}

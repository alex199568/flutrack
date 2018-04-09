package version.evening.canvas.flutrack

import android.content.Context
import dagger.Module
import dagger.Provides
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.main.MainViewModel
import version.evening.canvas.flutrack.map.MapViewModel

@Module
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context = context

    @AppScope
    @Provides
    fun provideSchedulers(): SchedulersWrapper = SchedulersWrapper()
}

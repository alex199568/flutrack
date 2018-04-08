package version.evening.canvas.flutrack

import android.content.Context
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context = context

    @AppScope
    @Provides
    fun provideSchedulers(): SchedulersWrapper = SchedulersWrapper()
}

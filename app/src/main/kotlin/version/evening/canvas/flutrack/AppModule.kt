package version.evening.canvas.flutrack

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context = context
}

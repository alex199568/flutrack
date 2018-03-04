package version.evening.canvas.flutrack

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class FlutrackApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}

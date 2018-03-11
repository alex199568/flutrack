package version.evening.canvas.flutrack

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulersWrapper {
    fun android(): Scheduler = AndroidSchedulers.mainThread()
    fun io(): Scheduler = Schedulers.io()
}

package version.evening.canvas.flutrack.map

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import version.evening.canvas.flutrack.doOnFirst

const val RETRY_TIMES = 3L

class MapViewModel(model: MapModel) {
    private val tweetsSubject: Subject<MapTweet> = ReplaySubject.create()
    private val errorSubject: Subject<Unit> = BehaviorSubject.create()
    private val firstSubject: Subject<Unit> = BehaviorSubject.create()

    val tweetsObservable: Observable<MapTweet> = tweetsSubject
    val errorObservable: Observable<Unit> = errorSubject
    val firstObservable: Observable<Unit> = firstSubject

    init {
        model
                .requestAll()
                .retry(RETRY_TIMES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnFirst { firstSubject.onNext(Unit) }
                .subscribe(
                        { tweetsSubject.onNext(it) },
                        {
                            errorSubject.onNext(Unit)
                        }
                )
    }
}

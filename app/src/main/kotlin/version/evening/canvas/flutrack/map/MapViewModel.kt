package version.evening.canvas.flutrack.map

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import version.evening.canvas.flutrack.doOnFirst

class MapViewModel(private val model: MapModel, private val schedulersWrapper: version.evening.canvas.flutrack.SchedulersWrapper) {
    private val tweetsSubject: Subject<MapTweet> = ReplaySubject.create()
    private val errorSubject: Subject<Unit> = BehaviorSubject.create()
    private val firstSubject: Subject<Unit> = BehaviorSubject.create()

    val tweetsObservable: Observable<MapTweet> = tweetsSubject
    val errorObservable: Observable<Unit> = errorSubject
    val firstObservable: Observable<Unit> = firstSubject

    fun requestTweets() {
        model
                .requestAll()
                .subscribeOn(schedulersWrapper.io())
                .observeOn(schedulersWrapper.android())
                .doOnFirst { firstSubject.onNext(Unit) }
                .subscribe(
                        { tweetsSubject.onNext(it) },
                        { errorSubject.onNext(Unit) }
                )
    }
}

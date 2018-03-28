package version.evening.canvas.flutrack.map

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject

class MapViewModel(
        private val model: MapModel,
        private val schedulersWrapper: version.evening.canvas.flutrack.SchedulersWrapper
) {
    private val tweetsSubject: Subject<MapTweet> = ReplaySubject.create()
    private val errorSubject = ReplaySubject.create<Unit>()

    val tweetsObservable: Observable<MapTweet> = tweetsSubject
    val errorObservable: Observable<Unit> = errorSubject

    fun requestTweets() {
        model
                .requestAll()
                .subscribeOn(schedulersWrapper.io())
                .observeOn(schedulersWrapper.android())
                .subscribe(
                        { tweetsSubject.onNext(it) },
                        { errorSubject.onNext(Unit) }
                )
    }
}

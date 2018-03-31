package version.evening.canvas.flutrack.map

import io.reactivex.disposables.CompositeDisposable
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

class MapPresenter(
        private val storage: MemoryFlutweetsStorage,
        private val schedulers: SchedulersWrapper,
        private val view: MapContract.View
) : MapContract.Presenter {
    private val pendingMapTweets = mutableListOf<MapTweet>()

    override fun onViewIsReady() {
        pendingMapTweets.apply {
            forEach { view.showMapTweet(it) }
            pendingMapTweets.clear()
        }
    }

    private val disposable = CompositeDisposable()

    override fun start() {
        disposable.add(storage.asObservable()
                .map { MapTweet(it) }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.android())
                .subscribe {
                    if (!view.showMapTweet(it)) {
                        pendingMapTweets.add(it)
                    }
                })
    }

    override fun stop() {
        disposable.clear()
    }
}

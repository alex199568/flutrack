package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

private const val STATS_KEY = "DashboardStats"

class DashboardPresenter(
        private val storage: MemoryFlutweetsStorage,
        private val schedulers: SchedulersWrapper,
        private val view: DashboardContract.View
) : DashboardContract.Presenter {
    private val disposables = CompositeDisposable()

    private var stats: DashboardStats? = null

    override fun start() {
        if (stats != null) {
            return
        }
        disposables.add(
                processTweets()
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.android())
                        .subscribe { stats ->
                            this.stats = stats
                            view.showStats(stats)
                        }
        )
    }

    override fun stop() {
        disposables.clear()
    }

    override fun saveState(outState: Bundle) {
        if (stats != null) {
            outState.putParcelable(STATS_KEY, stats)
            stats = null
        }
    }

    override fun restoreState(state: Bundle) {
        if (stats != null) {
            return
        }
        state.getParcelable<DashboardStats>(STATS_KEY)?.let {
            stats = it
            view.showStats(it)
        }
    }

    private fun processTweets(): Single<DashboardStats> {
        return storage.asObservable()
                .collect({ DashboardStatsState() }, { state, tweet -> state.processTweet(tweet) })
                .map { it.asDashboardStats() }
    }
}

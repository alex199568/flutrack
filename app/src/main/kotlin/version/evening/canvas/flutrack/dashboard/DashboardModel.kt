package version.evening.canvas.flutrack.dashboard

import io.reactivex.Single
import version.evening.canvas.flutrack.api.FlutrackAll

class DashboardModel(private val flutrackAll: FlutrackAll) {
    fun requestAll(): Single<List<DashboardTweet>> =
            flutrackAll
                    .results()
                    .flatMapIterable { it }
                    .map { DashboardTweet(it) }
                    .toList()
}

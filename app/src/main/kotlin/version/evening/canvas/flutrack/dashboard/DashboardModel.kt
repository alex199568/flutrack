package version.evening.canvas.flutrack.dashboard

import io.reactivex.Single
import version.evening.canvas.flutrack.api.FlutrackAll

class DashboardModel(private val flutrackAll: FlutrackAll) {
    fun requestData(): Single<DashboardStats> {
        return flutrackAll
                .results()
                .flatMapIterable { it }
                .count()
                .map { DashboardStats(it) }
    }
}

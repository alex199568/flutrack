package version.evening.canvas.flutrack.dashboard

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import version.evening.canvas.flutrack.SchedulersWrapper

class DashboardViewModel(model: DashboardModel, schedulersWrapper: SchedulersWrapper) {
    private val onErrorSubject = ReplaySubject.create<Unit>()
    val onErrorObservable: Observable<Unit> = onErrorSubject

    private val dashboardStatsSubject = ReplaySubject.create<DashboardStats>()
    val dashboardStatsObservable: Observable<DashboardStats> = dashboardStatsSubject

    init {
        model.requestData()
                .subscribeOn(schedulersWrapper.io())
                .observeOn(schedulersWrapper.android())
                .subscribe(
                        { dashboardStatsSubject.onNext(it) },
                        { onErrorSubject.onNext(Unit) }
                )
    }
}

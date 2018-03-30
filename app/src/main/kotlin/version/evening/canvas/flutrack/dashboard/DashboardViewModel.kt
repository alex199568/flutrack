package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import version.evening.canvas.flutrack.SchedulersWrapper
import kotlin.math.roundToInt

const val PERCENT = 100.0

const val STATS_KEY = "Stats"

class DashboardViewModel(private val model: DashboardModel, private val schedulersWrapper: SchedulersWrapper) {
    private val dashboardStatsSubject = ReplaySubject.create<DashboardStats>()
    val dashboardStatsObservable: Observable<DashboardStats> = dashboardStatsSubject

    private val errorSubject = ReplaySubject.create<Unit>()
    val errorObservable: Observable<Unit> = errorSubject

    private var totalTweets = 0
    private var totalSymptoms = 0
    private val symptomsCounter = mutableMapOf<Symptom, Int>()

    private lateinit var stats: DashboardStats

    private var stateRestored = false

    val dashboardStats: DashboardStats? by lazy { if (::stats.isInitialized) stats else null }

    fun requestDashboardStats() {
        if (stateRestored) {
            return
        }

        Symptom.values().forEach { symptomsCounter[it] = 0 }

        model.requestAll()
                .subscribeOn(schedulersWrapper.io())
                .observeOn(schedulersWrapper.android())
                .subscribe({
                    try {
                        it.forEach { tweet ->
                            ++totalTweets
                            Symptom.values().forEach {
                                if (tweet.text.contains(it.symptom, true)) {
                                    symptomsCounter[it] = symptomsCounter[it]?.plus(1) ?: 0
                                    ++totalSymptoms
                                }
                            }
                        }

                        val mostFrequent = symptomsCounter.maxBy { it.value }
                        val mostFrequentSymptom = mostFrequent?.key
                                ?: throw IllegalStateException("no most frequent symptom")
                        val mostFrequentNumber = mostFrequent.value

                        stats = DashboardStats(
                                totalTweets,
                                mostFrequentSymptom.symptom.capitalize(),
                                mostFrequentNumber,
                                (mostFrequentNumber.toDouble() / totalSymptoms.toDouble() * PERCENT).roundToInt(),
                                totalSymptoms
                        )

                        dashboardStatsSubject.onNext(stats)
                    } catch (e: IllegalStateException) {
                        dashboardStatsSubject.onError(e)
                    }
                }, { errorSubject.onNext(Unit) })
    }

    fun restoreStats(savedState: Bundle) {
        stateRestored = true
        savedState.getParcelable<DashboardStats>(STATS_KEY)?.let {
            stats = it
            dashboardStatsSubject.onNext(stats)
            return
        }
        errorSubject.onNext(Unit)
    }
}

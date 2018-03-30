package version.evening.canvas.flutrack.dashboard

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import kotlin.math.roundToInt

private const val PERCENT = 100.0

class DashboardPresenter(
        private val storage: MemoryFlutweetsStorage,
        private val schedulers: SchedulersWrapper,
        private val view: DashboardContract.View
) : DashboardContract.Presenter {
    private var totalTweets = 0
    private var totalSymptoms = 0

    private val disposables = CompositeDisposable()

    override fun start() {
        disposables.add(
                processTweets()
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.android())
                        .subscribe { stats -> view.showStats(stats) }
        )
    }

    override fun stop() {
        disposables.clear()
    }

    private fun processTweets(): Single<DashboardStats> {
        return storage.asObservable().collect({
            val symptomsState = mutableMapOf<Symptom, Int>()
            Symptom.values().forEach { symptomsState[it] = 0 }
            symptomsState
        }, { state, tweet ->
            ++totalTweets
            Symptom.values().forEach {
                if (tweet.tweetText?.contains(it.symptom, true) == true) {
                    state[it] = state[it]?.plus(1) ?: 0
                    ++totalSymptoms
                }
            }
        }).map {
            val mostFrequent = it.maxBy { it.value }
            val mostFrequentSymptom = mostFrequent?.key
                    ?: throw IllegalStateException("no most frequent symptom")
            val mostFrequentNumber = mostFrequent.value

            DashboardStats(
                    totalTweets,
                    mostFrequentSymptom.symptom.capitalize(),
                    mostFrequentNumber,
                    (mostFrequentNumber.toDouble() / totalSymptoms.toDouble() * PERCENT).roundToInt(),
                    totalSymptoms
            )
        }
    }
}

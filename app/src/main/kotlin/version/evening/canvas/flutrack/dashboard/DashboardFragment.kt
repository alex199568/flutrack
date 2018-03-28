package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.most_frequent_symptom.view.mostFrequentSymptom
import kotlinx.android.synthetic.main.most_frequent_symptom.view.mostFrequentSymptomValue
import kotlinx.android.synthetic.main.percentage.view.percentageValue
import kotlinx.android.synthetic.main.total_symptoms.totalSymptomsValue
import kotlinx.android.synthetic.main.total_tweets.view.tweetsValue
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import javax.inject.Inject

class DashboardFragment : Fragment() {
    @Inject
    lateinit var viewModel: DashboardViewModel

    private val errorSubject = BehaviorSubject.create<Unit>()
    val errorObservable: Observable<Unit> = errorSubject

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (activity?.application as FlutrackApplication).appComponent

        DaggerDashboardComponent.builder().dashboardModule(DashboardModule(
                appComponent.flutrackAll(), appComponent.schedulers()
        )).build().inject(this)

        if (savedInstanceState == null) {
            viewModel.requestDashboardStats()
        } else {
            viewModel.restoreStats(savedInstanceState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables.add(viewModel.dashboardStatsObservable.subscribe({
            view.apply {
                tweetsValue.text = it.numberOfTweets.toString()
                mostFrequentSymptom.text = it.mostFrequentSymptom
                mostFrequentSymptomValue.text = it.mostFrequentSymptomNumber.toString()
                percentageValue.text = it.mostFrequentSymptomPercentage.toString()
                totalSymptomsValue.text = it.totalSymptoms.toString()
            }
        }, { errorSubject.onNext(Unit) }))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATS_KEY, viewModel.dashboardStats)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}

fun createDashboardFragment(): DashboardFragment = DashboardFragment()

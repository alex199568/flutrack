package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (activity?.application as FlutrackApplication).appComponent

        DaggerDashboardComponent.builder().dashboardModule(DashboardModule(
                appComponent.flutrackAll(), appComponent.schedulers()
        )).build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dashboardStatsObservable.subscribe {
            view.apply {
                tweetsValue.text = it.numberOfTweets.toString()
                mostFrequentSymptom.text = it.mostFrequentSymptom
                mostFrequentSymptomValue.text = it.mostFrequentSymptomNumber.toString()
                percentageValue.text = it.mostFrequentSymptomPercentange.toString()
                totalSymptomsValue.text = it.totalSymptoms.toString()
            }
        }
    }
}

fun createDashboardFragment(): DashboardFragment = DashboardFragment()
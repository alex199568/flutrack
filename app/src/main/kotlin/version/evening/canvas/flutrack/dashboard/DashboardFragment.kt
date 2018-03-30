package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.most_frequent_symptom.mostFrequentSymptom
import kotlinx.android.synthetic.main.most_frequent_symptom.mostFrequentSymptomValue
import kotlinx.android.synthetic.main.percentage.percentageValue
import kotlinx.android.synthetic.main.total_symptoms.totalSymptomsValue
import kotlinx.android.synthetic.main.total_tweets.tweetsValue
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import version.evening.canvas.flutrack.main.MainActivity
import javax.inject.Inject

class DashboardFragment : Fragment(), DashboardContract.View {
    @Inject
    lateinit var presenter: DashboardContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerDashboardComponent.builder()
                .appComponent((activity?.application as FlutrackApplication).appComponent)
                .dashboardModule(DashboardModule(this))
                .build().inject(this)

        (activity as MainActivity).presenter.registerDependentPresenter(presenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }

    override fun showStats(stats: DashboardStats) {
        tweetsValue.text = stats.numberOfTweets.toString()
        mostFrequentSymptom.text = stats.mostFrequentSymptom
        mostFrequentSymptomValue.text = stats.mostFrequentSymptomNumber.toString()
        percentageValue.text = stats.mostFrequentSymptomPercentage.toString()
        totalSymptomsValue.text = stats.totalSymptoms.toString()
    }
}

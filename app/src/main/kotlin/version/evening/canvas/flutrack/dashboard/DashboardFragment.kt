package version.evening.canvas.flutrack.dashboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
import javax.inject.Inject

private const val TAG = "dashboard"

fun addDashboardFragment(fragmentManager: FragmentManager, container: Int) {
    if (fragmentManager.findFragmentByTag(TAG) == null) {
        fragmentManager.beginTransaction().add(container, DashboardFragment(), TAG).commit()
    }
}

class DashboardFragment : Fragment() {
    @Inject
    lateinit var viewmodelFactory: DashboardViewModel.Factory

    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        activity?.let {
            (it.application as FlutrackApplication).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewmodelFactory).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.statsData.value?.let { showStats(it) }
        viewModel.statsData.observe(this, Observer { it?.let { showStats(it) } })
    }

    private fun showStats(stats: DashboardStats) {
        tweetsValue.text = stats.numberOfTweets.toString()
        mostFrequentSymptom.text = stats.mostFrequentSymptom
        mostFrequentSymptomValue.text = stats.mostFrequentSymptomNumber.toString()
        percentageValue.text = stats.mostFrequentSymptomPercentage.toString()
        totalSymptomsValue.text = stats.totalSymptoms.toString()
    }
}

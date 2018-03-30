package version.evening.canvas.flutrack.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.viewPager
import version.evening.canvas.flutrack.AboutFragment
import version.evening.canvas.flutrack.ErrorDialogFragment
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import javax.inject.Inject

private const val ABOUT_TAG = "about_dialog"
private const val ERROR_TAG = "error_dialog"

class MainActivity : AppCompatActivity(), MainContract.View {
    @Inject
    lateinit var presenter: MainContract.Presenter

    private lateinit var dashboardFragment: DashboardFragment

    override fun showAboutDialog() {
        AboutFragment().show(supportFragmentManager, ABOUT_TAG)
    }

    override fun showErrorDialog() {
        ErrorDialogFragment().let {
            it.show(supportFragmentManager, ERROR_TAG)
            it.retryObservable.subscribe { presenter.errorRetry() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerMainComponent.builder()
                .appComponent((application as FlutrackApplication).appComponent)
                .mainModule(MainModule(this))
                .build().inject(this)

        presenter.start()

        dashboardFragment = DashboardFragment()

        viewPager.adapter = MainAdapter(
                listOf(dashboardFragment),
                listOf(getString(R.string.bottom_navigation_dashboard)),
                supportFragmentManager
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.about -> {
                presenter.actionAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }
}

package version.evening.canvas.flutrack.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.viewPager
import version.evening.canvas.flutrack.AboutDialogFragment
import version.evening.canvas.flutrack.ErrorDialogFragment
import version.evening.canvas.flutrack.R
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import version.evening.canvas.flutrack.dashboard.addDashboardFragment
import version.evening.canvas.flutrack.map.MapFragment
import version.evening.canvas.flutrack.map.addMapFragment

private const val ABOUT_TAG = "about_dialog"
private const val ERROR_TAG = "error_dialog"

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.onError.observe(this, Observer<Unit> { showErrorDialog() })

        dashboardFragment = DashboardFragment()
        mapFragment = MapFragment()

        if (resources.getBoolean(R.bool.isTablet)) {
            addMapFragment(supportFragmentManager, R.id.mapContainer)
            addDashboardFragment(supportFragmentManager, R.id.dashboardContainer)
        } else {
            viewPager?.adapter = MainAdapter(
                    listOf(mapFragment, dashboardFragment),
                    listOf(getString(R.string.bottom_navigation_map), getString(R.string.bottom_navigation_dashboard)),
                    supportFragmentManager
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showErrorDialog() {
        supportFragmentManager?.let {
            val errorDialog = ErrorDialogFragment()
            errorDialog.show(it, ERROR_TAG)
            errorDialog.retryObservable.subscribe { mainViewModel.requestFlutweets() }
        }
    }

    private fun showAboutDialog() {
        supportFragmentManager?.let {
            AboutDialogFragment().show(it, ABOUT_TAG)
        }
    }
}

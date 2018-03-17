package version.evening.canvas.flutrack

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import version.evening.canvas.flutrack.dashboard.createDashboardFragment
import version.evening.canvas.flutrack.map.MapFragment
import version.evening.canvas.flutrack.map.createMapFragment

private const val MAP_TAG = "map"
private const val DASHBOARD_TAG = "dashboard"
private const val DATA_ERROR_TAG = "data_error"
private const val ABOUT_TAG = "about"

private const val CURRENT_TAG_EXTRA = "CurrentTag"
private const val LAST_CONTENT_TAG_EXTRA = "LastContentTag"

data class FragmentWrapper(val fragment: Fragment, val tag: String)

class MainActivity : AppCompatActivity() {
    private lateinit var aboutFragment: AboutFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var dashboardFragment: DashboardFragment

    private lateinit var dataErrorFragment: DataErrorFragment

    private var lastContentFragmentWrapper: FragmentWrapper? = null

    private var currentFragmentTag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboutFragment = findOrCreateFragment(ABOUT_TAG, { createAboutFragment() }) as AboutFragment
        mapFragment = findOrCreateFragment(MAP_TAG, { createMapFragment() }) as MapFragment
        dashboardFragment = findOrCreateFragment(DASHBOARD_TAG, { createDashboardFragment() }) as DashboardFragment
        dataErrorFragment = findOrCreateFragment(DATA_ERROR_TAG, { createDataErrorFragment() }) as DataErrorFragment

        bottomNavigation.setOnNavigationItemSelectedListener {
            val (fragment, tag) = when (it.itemId) {
                R.id.map -> FragmentWrapper(mapFragment, MAP_TAG)
                R.id.dashboard -> FragmentWrapper(dashboardFragment, DASHBOARD_TAG)
                else -> throw IllegalArgumentException("unexpected bottom navigation item")
            }
            setContentFragment(fragment, tag)
            true
        }
        bottomNavigation.selectedItemId = R.id.map

        setContentFragment(mapFragment, MAP_TAG)

        mapFragment.dataErrorObservable.subscribe {
            setErrorFragment(mapFragment, MAP_TAG)
        }
        dashboardFragment.erroObservable.subscribe {
            setErrorFragment(dashboardFragment, DASHBOARD_TAG)
        }
        dataErrorFragment.retryObservable.subscribe {
            lastContentFragmentWrapper?.let {
                setContentFragment(it.fragment, it.tag)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(CURRENT_TAG_EXTRA, currentFragmentTag)
        lastContentFragmentWrapper?.let {
            outState?.putString(LAST_CONTENT_TAG_EXTRA, it.tag)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val lastFragmentTag = savedInstanceState?.getString(CURRENT_TAG_EXTRA)
        if (lastFragmentTag != null) {
            setContentFragment(resolveFragmentByTag(lastFragmentTag), lastFragmentTag)
        }
        val lastContentFragmentTag = savedInstanceState?.getString(LAST_CONTENT_TAG_EXTRA)
        if (lastContentFragmentTag != null) {
            lastContentFragmentWrapper = FragmentWrapper(
                    resolveFragmentByTag(lastContentFragmentTag), lastContentFragmentTag
            )
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.about -> {
                aboutFragment.show(supportFragmentManager, ABOUT_TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setContentFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(R.id.mainContentContainer, fragment, tag).commit()
        currentFragmentTag = tag
    }

    private fun setErrorFragment(contentFragment: Fragment, tag: String) {
        lastContentFragmentWrapper = FragmentWrapper(contentFragment, tag)
        setContentFragment(dataErrorFragment, DATA_ERROR_TAG)
    }

    private fun findOrCreateFragment(tag: String, createFunction: () -> Fragment): Fragment =
            supportFragmentManager.findFragmentByTag(tag) ?: createFunction()

    private fun resolveFragmentByTag(tag: String): Fragment =
            when (tag) {
                MAP_TAG -> mapFragment
                ABOUT_TAG -> aboutFragment
                DASHBOARD_TAG -> dashboardFragment
                DATA_ERROR_TAG -> dataErrorFragment
                else -> throw IllegalArgumentException("unknown fragment tag $tag")
            }
}

package version.evening.canvas.flutrack

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import version.evening.canvas.flutrack.dashboard.createDashboardFragment
import version.evening.canvas.flutrack.map.MapFragment
import version.evening.canvas.flutrack.map.createMapFragment

class MainActivity : AppCompatActivity() {
    private lateinit var aboutFragment: AboutFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var dashboardFragment: DashboardFragment

    private lateinit var dataErrorFragment: DataErrorFragment

    private lateinit var lastContentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboutFragment = createAboutFragment()
        mapFragment = createMapFragment()
        dashboardFragment = createDashboardFragment()
        dataErrorFragment = createDataErrorFragment()

        bottomNavigation.setOnNavigationItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.map -> mapFragment
                R.id.dashboard -> dashboardFragment
                R.id.about -> aboutFragment
                else -> null
            }
            if (fragment == null) {
                false
            } else {
                setContentFragment(fragment)
                true
            }
        }
        bottomNavigation.selectedItemId = R.id.map

        setContentFragment(mapFragment)

        mapFragment.dataErrorObservable.subscribe {
            setErrorFragment(mapFragment)
        }
        dashboardFragment.erroObservable.subscribe {
            setErrorFragment(dashboardFragment)
        }
        dataErrorFragment.retryObservable.subscribe {
            setContentFragment(lastContentFragment)
        }
    }

    private fun setContentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainContentContainer, fragment).commit()
    }

    private fun setErrorFragment(contentFragment: Fragment) {
        lastContentFragment = contentFragment
        setContentFragment(dataErrorFragment)
    }
}

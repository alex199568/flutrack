package version.evening.canvas.flutrack

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import version.evening.canvas.flutrack.dashboard.DashboardFragment
import version.evening.canvas.flutrack.dashboard.createDashboardFragment
import version.evening.canvas.flutrack.map.MapFragment
import version.evening.canvas.flutrack.map.createMapFragment

private const val ABOUT_TAG = "about"

class MainActivity : AppCompatActivity() {
    private lateinit var aboutFragment: AboutFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var dashboardFragment: DashboardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboutFragment = createAboutFragment()
        mapFragment = createMapFragment()
        dashboardFragment = createDashboardFragment()

        viewPager.adapter = MainAdapter(
                listOf(mapFragment, dashboardFragment),
                listOf(getString(R.string.bottom_navigation_map), getString(R.string.bottom_navigation_dashboard)),
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
                aboutFragment.show(supportFragmentManager, ABOUT_TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

class MainAdapter(
        private val fragments: List<Fragment>,
        private val titles: List<CharSequence>,
        fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}

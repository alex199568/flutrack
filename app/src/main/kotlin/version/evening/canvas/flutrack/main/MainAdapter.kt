package version.evening.canvas.flutrack.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

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


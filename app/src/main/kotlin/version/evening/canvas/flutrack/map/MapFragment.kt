package version.evening.canvas.flutrack.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import version.evening.canvas.flutrack.data.FluTweet
import javax.inject.Inject

private const val TAG = "map"

fun addMapFragment(fragmentManager: FragmentManager, container: Int) {
    if (fragmentManager.findFragmentByTag(TAG) == null) {
        fragmentManager.beginTransaction().add(container, MapFragment(), TAG).commit()
    }
}

class MapFragment : SupportMapFragment() {
    private lateinit var map: GoogleMap
    private lateinit var infoWindowAdapter: TweetInfoWindowAdapter
    private lateinit var viewModel: MapViewModel

    @Inject
    lateinit var viewModelFactory: MapViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        (activity?.application as FlutrackApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
                ?: throw IllegalStateException("google maps didn't create view")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getMapAsync {
            map = it.apply {
                clear()
                setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
                uiSettings.isMapToolbarEnabled = false
            }
            context?.let {
                initInfoWindowAdapter(it)
                setupData()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showTweets(tweets: List<FluTweet>) {
        tweets.forEach { showMapTweet(MapTweet(it)) }
    }

    private fun showMapTweet(mapTweet: MapTweet) {
        val markerOptions = MarkerOptions().apply {
            position(mapTweet.latLng)
        }
        val marker = map.addMarker(markerOptions)
        infoWindowAdapter.registerMarkerData(marker, mapTweet)
    }

    private fun initInfoWindowAdapter(context: Context) {
        infoWindowAdapter = TweetInfoWindowAdapter(context)
        map.setInfoWindowAdapter(infoWindowAdapter)
    }

    private fun setupData() {
        viewModel.data.value?.let { showTweets(it) }
        viewModel.data.observe(this, Observer<List<FluTweet>> { it?.let { showTweets(it) } })
    }
}

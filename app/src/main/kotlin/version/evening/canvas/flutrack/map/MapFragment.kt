package version.evening.canvas.flutrack.map

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
import version.evening.canvas.flutrack.main.MainActivity
import javax.inject.Inject

private const val TAG = "map"

fun addMapFragment(fragmentManager: FragmentManager, container: Int) {
    if (fragmentManager.findFragmentByTag(TAG) == null) {
        fragmentManager.beginTransaction().add(container, MapFragment(), TAG).commit()
    }
}

class MapFragment : SupportMapFragment(), MapContract.View {
    @Inject
    lateinit var presenter: MapContract.Presenter

    private lateinit var map: GoogleMap
    private var infoWindowAdapter: TweetInfoWindowAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        val appComponent = (activity?.application as FlutrackApplication).appComponent

        DaggerMapComponent.builder()
                .appComponent(appComponent)
                .mapModule(MapModule(this))
                .build().inject(this)
    }

    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)
        (activity as MainActivity).presenter.registerDependentPresenter(presenter)
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
            context?.let { initInfoWindowAdapter(it) }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showMapTweet(mapTweet: MapTweet): Boolean {
        if (!this::map.isInitialized) {
            return false
        }
        if (infoWindowAdapter == null) {
            context?.let { initInfoWindowAdapter(it) }
            return false
        }
        val markerOptions = MarkerOptions().apply {
            position(mapTweet.latLng)
        }
        val marker = map.addMarker(markerOptions)
        infoWindowAdapter?.registerMarkerData(marker, mapTweet)
        return true
    }

    override fun onDestroyView() {
        presenter.stop()
        infoWindowAdapter = null
        super.onDestroyView()
    }

    private fun initInfoWindowAdapter(context: Context) {
        if (infoWindowAdapter != null) {
            return
        }
        infoWindowAdapter = TweetInfoWindowAdapter(context)
        presenter.onViewIsReady()
        map.setInfoWindowAdapter(infoWindowAdapter)
    }
}

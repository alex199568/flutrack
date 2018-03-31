package version.evening.canvas.flutrack.map

import android.os.Bundle
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

class MapFragment : SupportMapFragment(), MapContract.View {
    @Inject
    lateinit var presenter: MapContract.Presenter

    private lateinit var map: GoogleMap
    private lateinit var infoWindowAdapter: TweetInfoWindowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
                uiSettings.isMapToolbarEnabled = false
            }
            context?.let {
                infoWindowAdapter = TweetInfoWindowAdapter(it)
                presenter.onViewIsReady()
                map.setInfoWindowAdapter(infoWindowAdapter)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showMapTweet(mapTweet: MapTweet): Boolean {
        if (!this::infoWindowAdapter.isInitialized) {
            return false
        }
        val markerOptions = MarkerOptions().apply {
            position(mapTweet.latLng)
        }
        val marker = map.addMarker(markerOptions)
        infoWindowAdapter.registerMarkerData(marker, mapTweet)
        return true
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }
}

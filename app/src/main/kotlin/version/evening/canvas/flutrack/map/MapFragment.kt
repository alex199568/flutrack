package version.evening.canvas.flutrack.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.CompositeDisposable
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import javax.inject.Inject

class MapFragment : SupportMapFragment() {
    @Inject
    lateinit var viewModel: MapViewModel

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (activity?.application as FlutrackApplication).appComponent

        DaggerMapComponent.builder().mapModule(MapModule(
                appComponent.flutrackAll(), appComponent.schedulers()
        )).build().inject(this)

        viewModel.requestTweets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
                ?: throw IllegalStateException("google maps didn't create view")

        val infoWindowAdapter = TweetInfoWindowAdapter(context!!)

        getMapAsync { map ->
            viewModel.tweetsObservable.subscribe {
                val markerOptions = MarkerOptions().apply {
                    position(it.latLng)
                }
                val marker = map.addMarker(markerOptions)
                infoWindowAdapter.registerMarkerData(marker, it)
            }
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
            map.setInfoWindowAdapter(infoWindowAdapter)
            map.uiSettings.isMapToolbarEnabled = false
        }

        return view
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}

fun createMapFragment(): MapFragment = MapFragment()

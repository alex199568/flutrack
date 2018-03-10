package version.evening.canvas.flutrack.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import javax.inject.Inject

class MapFragment : Fragment() {
    private lateinit var mapView: MapView

    @Inject
    lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMapComponent.builder().mapModule(MapModule(
                (activity?.application as FlutrackApplication).appComponent.flutrackAll()
        )).build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_map, container, false)

        val infoWindowAdapter = TweetInfoWindowAdapter(context!!)

        this.mapView = view.mapView.apply {
            onCreate(savedInstanceState)
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
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorObservable.subscribe {
            Toast.makeText(context, "Could not load data", Toast.LENGTH_SHORT).show()
        }
        viewModel.firstObservable.subscribe {
            mapView.visibility = View.VISIBLE
            mapProgressBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}

fun createMapFragment(): MapFragment = MapFragment()

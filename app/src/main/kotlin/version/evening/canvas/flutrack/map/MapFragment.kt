package version.evening.canvas.flutrack.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import version.evening.canvas.flutrack.ErrorDialogFragment
import version.evening.canvas.flutrack.FlutrackApplication
import version.evening.canvas.flutrack.R
import javax.inject.Inject

private const val ERROR_TAG = "map_error_dialog"

class MapFragment : SupportMapFragment() {
    @Inject
    lateinit var viewModel: MapViewModel

    private val disposables = CompositeDisposable()

    private val errorSubject = BehaviorSubject.create<Unit>()
    val errorObservable: Observable<Unit> = errorSubject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (activity?.application as FlutrackApplication).appComponent

        DaggerMapComponent.builder().mapModule(MapModule(
                appComponent.flutrackAll(), appComponent.schedulers()
        )).build().inject(this)

        disposables.add(viewModel.errorObservable.subscribe { errorSubject.onNext(Unit) })

        viewModel.requestTweets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
                ?: throw IllegalStateException("google maps didn't create view")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val infoWindowAdapter = TweetInfoWindowAdapter(context!!)
        getMapAsync { map ->
            disposables.add(viewModel.tweetsObservable.subscribe {
                val markerOptions = MarkerOptions().apply {
                    position(it.latLng)
                }
                val marker = map.addMarker(markerOptions)
                infoWindowAdapter.registerMarkerData(marker, it)
            })

            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
            map.setInfoWindowAdapter(infoWindowAdapter)
            map.uiSettings.isMapToolbarEnabled = false
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    fun retry() {
        viewModel.requestTweets()
    }
}

fun createMapFragment(): MapFragment = MapFragment()

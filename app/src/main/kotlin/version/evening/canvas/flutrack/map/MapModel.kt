package version.evening.canvas.flutrack.map

import io.reactivex.Observable
import version.evening.canvas.flutrack.api.FlutrackAll

class MapModel(private val flutrackAll: FlutrackAll) {
    fun requestAll(): Observable<MapTweet> =
            flutrackAll
                    .results()
                    .flatMapIterable { it }
                    .map { MapTweet(it) }

}

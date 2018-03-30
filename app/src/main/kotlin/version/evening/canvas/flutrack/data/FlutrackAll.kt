package version.evening.canvas.flutrack.data

import io.reactivex.Observable
import retrofit2.http.GET

interface FlutrackAll {
    @GET("/results.json")
    fun results(): Observable<List<FluTweet>>
}

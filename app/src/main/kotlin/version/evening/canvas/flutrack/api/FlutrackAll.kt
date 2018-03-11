package version.evening.canvas.flutrack.api

import io.reactivex.Observable
import retrofit2.http.GET

interface FlutrackAll {
    @GET("/results.json")
    fun results(): Observable<List<FluTweet>>
}

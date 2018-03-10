package version.evening.canvas.flutrack.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.tweet_info.view.*
import version.evening.canvas.flutrack.R

class TweetInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {
    @SuppressLint("InflateParams")
    private val view: View = LayoutInflater.from(context).inflate(R.layout.tweet_info, null)

    private val markerMap: MutableMap<Marker, MapTweet> = mutableMapOf()

    fun registerMarkerData(marker: Marker, mapTweet: MapTweet) {
        markerMap[marker] = mapTweet
    }

    override fun getInfoContents(marker: Marker): View? {
        markerMap[marker]?.let {
            return view.apply {
                userNameValue.text = it.userName
                tweetDate.text = it.tweetDate
                tweetText.text = it.tweetText
                if (it.aggravation) {
                    aggravationIndicator.visibility = View.VISIBLE
                }
            }
        }
        return null
    }

    override fun getInfoWindow(marker: Marker?): View? = null
}

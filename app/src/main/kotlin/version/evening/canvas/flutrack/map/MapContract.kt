package version.evening.canvas.flutrack.map

import version.evening.canvas.flutrack.BaseContract

interface MapContract : BaseContract {
    interface View : BaseContract.View {
        fun showMapTweet(mapTweet: MapTweet)
    }

    interface Presenter : BaseContract.Presenter
}

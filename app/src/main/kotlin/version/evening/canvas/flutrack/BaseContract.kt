package version.evening.canvas.flutrack

import android.os.Bundle

interface BaseContract {
    interface View

    interface Presenter {
        fun start()
        fun stop()
        fun saveState(outState: Bundle) = Unit
        fun restoreState(state: Bundle) = Unit
    }
}

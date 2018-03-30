package version.evening.canvas.flutrack

interface BaseContract {
    interface View

    interface Presenter {
        fun start()
        fun stop()
    }
}

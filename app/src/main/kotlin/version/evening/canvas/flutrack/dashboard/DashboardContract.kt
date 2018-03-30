package version.evening.canvas.flutrack.dashboard

import version.evening.canvas.flutrack.BaseContract

interface DashboardContract : BaseContract {
    interface View : BaseContract.View {
        fun showStats(stats: DashboardStats)
        fun showLoading()
    }

    interface Presenter : BaseContract.Presenter
}

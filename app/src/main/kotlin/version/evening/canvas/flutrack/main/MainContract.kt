package version.evening.canvas.flutrack.main

import version.evening.canvas.flutrack.BaseContract

interface MainContract : BaseContract {
    interface View : BaseContract.View {
        fun showAboutDialog()
        fun showErrorDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun registerDependentPresenter(presenter: BaseContract.Presenter)
        fun actionAbout()
        fun errorRetry()
    }
}

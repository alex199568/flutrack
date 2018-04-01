package version.evening.canvas.flutrack.main

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import version.evening.canvas.flutrack.BaseContract
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

class MainPresenter(
        private val flutrackAll: FlutrackAll,
        private val memoryStorage: MemoryFlutweetsStorage,
        private val schedulers: SchedulersWrapper,
        private val view: MainContract.View
) : MainContract.Presenter {
    private val disposables = CompositeDisposable()
    private val dependentPresenters = mutableListOf<BaseContract.Presenter>()

    private var presentersNotified = false

    override fun start() {
        disposables.add(flutrackAll
                .results()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.android())
                .subscribe({
                    memoryStorage.save(it)
                    dependentPresenters.forEach { it.start() }
                    presentersNotified = true
                }, { view.showErrorDialog() }))
    }

    override fun stop() {
        memoryStorage.clear()
        presentersNotified = false
        disposables.clear()
    }

    override fun saveState(outState: Bundle) {
        view.dismissErrorDialog()
    }

    override fun registerDependentPresenter(presenter: BaseContract.Presenter) {
        dependentPresenters.add(presenter)
        if (presentersNotified) {
            presenter.start()
        }
    }

    override fun actionAbout() {
        view.showAboutDialog()
    }

    override fun errorRetry() {
        stop()
        start()
    }
}

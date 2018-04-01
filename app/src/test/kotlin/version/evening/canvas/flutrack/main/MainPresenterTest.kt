package version.evening.canvas.flutrack.main

import android.accounts.NetworkErrorException
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.BaseContract
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import java.util.concurrent.TimeUnit

class MainPresenterTest {
    @Mock
    private lateinit var flutrackAll: FlutrackAll
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var view: MainContract.View
    @Mock
    private lateinit var presenter1: BaseContract.Presenter
    @Mock
    private lateinit var presenter2: BaseContract.Presenter

    private lateinit var presenter: MainContract.Presenter

    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet()

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        presenter = MainPresenter(flutrackAll, storage, schedulers, view)
    }

    @Test
    fun testSaveState() {
        presenter.saveState(Bundle())

        verify(view).dismissErrorDialog()
    }

    @Test
    fun testActionAbout() {
        presenter.actionAbout()

        verify(view).showAboutDialog()
    }

    @Test
    fun testStart() {
        val results = listOf(tweet1, tweet2)
        `when`(flutrackAll.results()).thenReturn(Observable.just(results))

        presenter.start()

        verify(storage).save(results)
    }

    @Test
    fun testPresentersAreNotified() {
        val results = listOf<FluTweet>()
        `when`(flutrackAll.results()).thenReturn(Observable.just(results))

        presenter.apply {
            registerDependentPresenter(presenter1)
            registerDependentPresenter(presenter2)
            start()
        }

        verify(presenter1).start()
        verify(presenter2).start()
    }

    @Test
    fun testPresentersAreNotifiedWhenRegisteringLate() {
        val results = listOf<FluTweet>()
        `when`(flutrackAll.results()).thenReturn(Observable.just(results))

        presenter.apply {
            start()
            registerDependentPresenter(presenter1)
            registerDependentPresenter(presenter2)
        }

        verify(presenter1).start()
        verify(presenter2).start()
    }

    @Test
    fun testStop() {
        val results = listOf<FluTweet>()
        `when`(flutrackAll.results()).thenReturn(Observable.just(results).delay(100, TimeUnit.MILLISECONDS))

        presenter.apply {
            registerDependentPresenter(presenter1)
            registerDependentPresenter(presenter2)
            start()
            stop()
        }

        verify(presenter1, never()).start()
        verify(presenter2, never()).start()
        verify(storage, never()).save(anyList())
    }

    @Test
    fun testError() {
        `when`(flutrackAll.results()).thenReturn(Observable.error(NetworkErrorException("no connection")))

        presenter.apply {
            registerDependentPresenter(presenter1)
            registerDependentPresenter(presenter2)
            start()
        }

        verify(view).showErrorDialog()

        verify(presenter1, never()).start()
        verify(presenter2, never()).start()
        verify(storage, never()).save(anyList())
    }

    @Test
    fun testStopClearsStorage() {
        presenter.stop()

        verify(storage).clear()
    }
}

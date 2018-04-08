package version.evening.canvas.flutrack.main

import android.arch.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.FlutrackAll
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import java.util.concurrent.TimeUnit
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

@RunWith(JUnit4::class)
class MainViewModelTest {
    @Mock
    private lateinit var flutrackAll: FlutrackAll
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var errorObserver: Observer<Unit>

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet("another")

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        viewModel = MainViewModel(flutrackAll, storage, schedulers)
    }

    @Test
    fun testRequestFluTweets() {
        val results = listOf(tweet1, tweet2)
        `when`(flutrackAll.results()).thenReturn(Observable.just(results))

        viewModel.requestFlutweets()

        verify(schedulers, times(2)).io()
        verify(storage).rewrite(results)
    }

    @Test
    fun testRequestIsNotMadeIfARequestIsInProgress() {
        val results = listOf(tweet1, tweet2)
        `when`(flutrackAll.results()).thenReturn(Observable.just(results).delay(100, TimeUnit.MILLISECONDS))

        viewModel.requestFlutweets()
        viewModel.requestFlutweets()

        verify(schedulers, times(2)).io()
    }

    @Test
    fun testEventIsEmittedOnError() {
        `when`(flutrackAll.results()).thenReturn(Observable.error(RuntimeException("error!")))

        viewModel.onError.observeForever(errorObserver)
        viewModel.requestFlutweets()

        verify(errorObserver).onChanged(Unit)
    }
}

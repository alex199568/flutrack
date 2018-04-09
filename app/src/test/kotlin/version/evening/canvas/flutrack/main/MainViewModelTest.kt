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
import java.util.concurrent.TimeUnit
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import version.evening.canvas.flutrack.data.FluTweetDao

@RunWith(JUnit4::class)
class MainViewModelTest {
    @Mock
    private lateinit var flutrackAll: FlutrackAll
    @Mock
    private lateinit var fluTweetDao: FluTweetDao
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var errorObserver: Observer<ErrorState>

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet(1, "another")

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())
    }

    @Test
    fun testRequestFluTweets() {
        val results = listOf(tweet1, tweet2)
        `when`(flutrackAll.results()).thenReturn(Observable.just(results))

        viewModel = MainViewModel(flutrackAll, fluTweetDao, schedulers)
        viewModel.errorData.observeForever(errorObserver)

        verify(schedulers, times(2)).io()
        verify(fluTweetDao).deleteAll()
        verify(fluTweetDao).save(results)
        verify(errorObserver).onChanged(ErrorState.DEFAULT)
    }

    @Test
    fun testRequestIsNotMadeIfARequestIsInProgress() {
        val results = listOf(tweet1, tweet2)
        `when`(flutrackAll.results()).thenReturn(Observable.just(results).delay(100, TimeUnit.MILLISECONDS))

        viewModel = MainViewModel(flutrackAll, fluTweetDao, schedulers)
        viewModel.requestFlutweets()

        verify(schedulers, times(2)).io()
    }

    @Test
    fun testEventIsEmittedOnError() {
        `when`(flutrackAll.results()).thenReturn(Observable.error(RuntimeException("error!")))

        viewModel = MainViewModel(flutrackAll, fluTweetDao, schedulers)
        viewModel.errorData.observeForever(errorObserver)

        verify(errorObserver).onChanged(ErrorState.ERROR_DIALOG_SHOWN)
    }
}

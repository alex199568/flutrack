package version.evening.canvas.flutrack.dashboard

import android.os.Bundle
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import java.util.concurrent.TimeUnit

class DashboardPresenterTest {
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var view: DashboardContract.View
    @Mock
    private lateinit var state: Bundle

    private lateinit var presenter: DashboardContract.Presenter

    private val tweet1 = FluTweet(tweetText = "flu")
    private val tweet2 = FluTweet(tweetText = "flu and chills")

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        presenter = DashboardPresenter(storage, schedulers, view)
    }

    @Test
    fun testStart() {
        `when`(storage.asObservable()).thenReturn(Observable.fromArray(tweet1, tweet2))

        presenter.start()

        val captor = argumentCaptor<DashboardStats>()
        verify(view).showStats(captor.capture())

        val stats = captor.firstValue

        assertEquals(2, stats.numberOfTweets)
        assertEquals(3, stats.totalSymptoms)
        assertEquals("Flu", stats.mostFrequentSymptom)
        assertEquals(2, stats.mostFrequentSymptomNumber)
        assertEquals(67, stats.mostFrequentSymptomPercentage)
    }

    @Test
    fun testStop() {
        `when`(storage.asObservable()).thenReturn(
                Observable.fromArray(tweet1, tweet2).delay(100, TimeUnit.MILLISECONDS)
        )

        presenter.apply {
            start()
            stop()
        }

        verifyZeroInteractions(view)
    }

    @Test
    fun testSaveState() {
        `when`(storage.asObservable()).thenReturn(Observable.fromArray(tweet1, tweet2))
        presenter.start()
        val captor = argumentCaptor<DashboardStats>()
        verify(view).showStats(captor.capture())

        val result = captor.firstValue

        presenter.saveState(state)

        verify(state).putParcelable(anyString(), eq(result))
    }

    @Test
    fun testSaveStateBeforeStart() {
        presenter.saveState(state)

        verifyZeroInteractions(state)
    }

    @Test
    fun testRestoreState() {
        val stats = DashboardStats()
        `when`(state.getParcelable<DashboardStats>(anyString())).thenReturn(stats)

        presenter.restoreState(state)

        verify(view).showStats(stats)
    }

    @Test
    fun testRestoreStateAfterStart() {
        `when`(storage.asObservable()).thenReturn(Observable.fromArray(tweet1, tweet2))

        presenter.start()

        verify(view).showStats(any())

        val stats = DashboardStats()
        `when`(state.getParcelable<DashboardStats>(anyString())).thenReturn(stats)
        presenter.restoreState(state)

        verifyNoMoreInteractions(view)
    }

    @Test
    fun testStartAfterRestore() {
        val stats = DashboardStats()
        `when`(state.getParcelable<DashboardStats>(anyString())).thenReturn(stats)
        presenter.restoreState(state)

        verify(view).showStats(any())

        presenter.start()

        verifyNoMoreInteractions(view)
    }
}

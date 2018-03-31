package version.evening.canvas.flutrack.map

import com.nhaarman.mockito_kotlin.any
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage
import java.util.concurrent.TimeUnit

class MapPresenterTest {
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var view: MapContract.View

    private lateinit var presenter: MapContract.Presenter

    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet("another")

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        presenter = MapPresenter(storage, schedulers, view)
    }

    @Test
    fun testStart() {
        `when`(storage.asObservable()).thenReturn(Observable.fromArray(tweet1, tweet2))
        `when`(view.showMapTweet(any())).thenReturn(false)

        presenter.start()

        verify(view).showMapTweet(MapTweet(tweet1))
        verify(view).showMapTweet(MapTweet(tweet2))
    }

    @Test
    fun testOnViewIsReady() {
        `when`(storage.asObservable()).thenReturn(Observable.fromArray(tweet1, tweet2))
        `when`(view.showMapTweet(any())).thenReturn(true)

        presenter.start()

        verify(view).showMapTweet(MapTweet(tweet1))
        verify(view).showMapTweet(MapTweet(tweet2))

        presenter.onViewIsReady()

        verifyNoMoreInteractions(view)
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

        verifyNoMoreInteractions(view)

        presenter.onViewIsReady()

        verifyNoMoreInteractions(view)
    }
}

package version.evening.canvas.flutrack.map

import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper

class MapViewModelTest {
    @Mock
    private lateinit var mapModel: MapModel
    @Mock
    private lateinit var schedulers: SchedulersWrapper

    private lateinit var mapViewModel: MapViewModel

    private val tweet1 = MapTweet("user1", "some tweet", 0.0, 1.0, "12 Jan 2018", true)
    private val tweet2 = MapTweet("user2", "another tweet", 2.0, -1.0, "13 Mar 2017", false)
    private val tweetsSubject = PublishSubject.create<MapTweet>()

    @Before
    fun setup() {
        initMocks(this)

        `when`(mapModel.requestAll()).thenReturn(tweetsSubject)
        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        mapViewModel = MapViewModel(mapModel, schedulers)
    }

    @Test
    fun testTweetsStream() {
        tweetsSubject.apply {
            onNext(tweet1)
            onNext(tweet2)
        }

        var numOfTweets = 0
        mapViewModel.tweetsObservable.subscribe {
            val compareWith = when (numOfTweets++) {
                0 -> tweet1
                1 -> tweet2
                else -> throw IllegalArgumentException("there are only two tweets")
            }
            assertEquals(compareWith, it)
        }
        mapViewModel.requestTweets()
    }

    @Test
    fun testOnError() {
        tweetsSubject.onError(Exception())

        var timesOnErrorCalled = 0

        mapViewModel.requestTweets()
        mapViewModel.errorObservable.subscribe { ++timesOnErrorCalled }

        assertEquals(1, timesOnErrorCalled)
    }
}

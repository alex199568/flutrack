package version.evening.canvas.flutrack.map

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.FlutrackAll

class MapModelTest {
    @Mock
    private lateinit var flutrackAll: FlutrackAll

    private lateinit var mapModel: MapModel
    private lateinit var fluTweetsObservable: Observable<List<FluTweet>>
    private val fluTweet1 = FluTweet("user1", "some tweet", 0.0, 0.0, "1520766833", "True")
    private val fluTweet2 = FluTweet("user2", "another tweet", 1.0, 1.0, "1520680433", "false")

    @Before
    fun setup() {
        initMocks(this)

        fluTweetsObservable = Observable.just(listOf(fluTweet1, fluTweet2))

        mapModel = MapModel(flutrackAll)
    }

    @Test
    fun testRequestAll() {
        `when`(flutrackAll.results()).thenReturn(fluTweetsObservable)

        var numOfMapTweet = 0

        mapModel.requestAll().subscribe {
            val compareWith = when (numOfMapTweet++) {
                0 -> fluTweet1
                1 -> fluTweet2
                else -> throw IllegalArgumentException("there are only two tweets")
            }
            assertTweetsAreEqual(compareWith, it)
        }
    }

    private fun assertTweetsAreEqual(fluTweet: FluTweet, mapTweet: MapTweet) {
        assertEquals(fluTweet.userName, mapTweet.userName)
        assertEquals(fluTweet.tweetText, mapTweet.tweetText)
        assertEquals(fluTweet.latitude, mapTweet.latLng.latitude)
        assertEquals(fluTweet.longitude, mapTweet.latLng.longitude)
    }
}

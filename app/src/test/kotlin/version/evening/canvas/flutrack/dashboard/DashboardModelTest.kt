package version.evening.canvas.flutrack.dashboard

import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.api.FluTweet
import version.evening.canvas.flutrack.api.FlutrackAll

class DashboardModelTest {
    private lateinit var dashboardModel: DashboardModel

    private val tweet1 = FluTweet("user1", "tweet1", 0.0, 1.0, "date", "0")
    private val tweet2 = FluTweet("user2", "tweet2", 2.0, 3.0, "date", "1")

    @Mock
    private lateinit var flutrackAll: FlutrackAll

    @Before
    fun setup() {
        initMocks(this)

        `when`(flutrackAll.results()).thenReturn(Observable.just(listOf(tweet1, tweet2)))

        dashboardModel = DashboardModel(flutrackAll)
    }

    @Test
    fun testRequestAll() {
        dashboardModel.requestAll().subscribe { tweets ->
            assertEquals(2, tweets.size)
            assertEquals(tweet1.tweetText, tweets.first().text)
            assertEquals(tweet2.tweetText, tweets[1].text)
        }
    }
}

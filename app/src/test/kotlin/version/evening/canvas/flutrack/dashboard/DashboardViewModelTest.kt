package version.evening.canvas.flutrack.dashboard

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper

class DashboardViewModelTest {
    @Mock
    private lateinit var model: DashboardModel
    @Mock
    private lateinit var schedulersWrapper: SchedulersWrapper

    private val tweet1 = DashboardTweet("sore throat and flu")
    private val tweet2 = DashboardTweet("chills and flu")

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulersWrapper.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulersWrapper.io()).thenReturn(Schedulers.io())

        `when`(model.requestAll()).thenReturn(Single.just(listOf(tweet1, tweet2)))

        viewModel = DashboardViewModel(model, schedulersWrapper)
    }

    @Test
    fun testTweetsAreCounted() {
        viewModel.dashboardStatsObservable.subscribe {
            assertEquals(2, it.numberOfTweets)
        }
    }

    @Test
    fun testSymptomsAreCounted() {
        viewModel.dashboardStatsObservable.subscribe {
            assertEquals(4, it.totalSymptoms)
        }
    }

    @Test
    fun testMostFrequentSymptom() {
        viewModel.dashboardStatsObservable.subscribe {
            assertTrue("flu".equals(it.mostFrequentSymptom, true))
            assertEquals(2, it.mostFrequentSymptomNumber)
        }
    }

    @Test
    fun testMostFrequentSymptomPercentage() {
        viewModel.dashboardStatsObservable.subscribe {
            assertEquals(50, it.mostFrequentSymptomPercentange)
        }
    }
}

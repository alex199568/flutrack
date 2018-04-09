package version.evening.canvas.flutrack.dashboard

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.argumentCaptor
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.SchedulersWrapper
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.MemoryFlutweetsStorage

class DashboardViewModelTest {
    @Mock
    private lateinit var storage: MemoryFlutweetsStorage
    @Mock
    private lateinit var schedulers: SchedulersWrapper
    @Mock
    private lateinit var observer: Observer<DashboardStats>

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel
    private lateinit var storageData: MutableLiveData<List<FluTweet>>

    private val tweet1 = FluTweet(tweetText = "flu")
    private val tweet2 = FluTweet(tweetText = "flu and chills")

    @Before
    fun setup() {
        initMocks(this)

        `when`(schedulers.android()).thenReturn(Schedulers.trampoline())
        `when`(schedulers.io()).thenReturn(Schedulers.trampoline())

        storageData = MutableLiveData()
        `when`(storage.data).thenReturn(storageData)

        viewModel = DashboardViewModel(storage, schedulers)
        viewModel.statsData.observeForever(observer)
    }

    @Test
    fun testDashboardStats() {
        val captor = argumentCaptor<DashboardStats>()

        storageData.postValue(listOf(tweet1, tweet2))

        verify(observer).onChanged(captor.capture())

        val stats = captor.firstValue

        assertEquals(2, stats.numberOfTweets)
        assertEquals(3, stats.totalSymptoms)
        assertEquals("Flu", stats.mostFrequentSymptom)
        assertEquals(2, stats.mostFrequentSymptomNumber)
        assertEquals(67, stats.mostFrequentSymptomPercentage)
    }
}

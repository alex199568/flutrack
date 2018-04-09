package version.evening.canvas.flutrack.map

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.argThat
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import version.evening.canvas.flutrack.data.FluTweet
import version.evening.canvas.flutrack.data.FluTweetDao

@RunWith(JUnit4::class)
class MapViewModelTest {
    @Mock
    private lateinit var fluTweetDao: FluTweetDao
    @Mock
    private lateinit var observer: Observer<List<FluTweet>>

    private lateinit var data: MutableLiveData<List<FluTweet>>

    private lateinit var viewModel: MapViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet(1, "another")

    @Before
    fun setup() {
        initMocks(this)

        data = MutableLiveData()
        `when`(fluTweetDao.getAll()).thenReturn(data)

        viewModel = MapViewModel(fluTweetDao)
        viewModel.data.observeForever(observer)
    }

    @Test
    fun testSave() {
        val results = listOf(tweet1, tweet2)

        data.postValue(results)

        verify(observer).onChanged(results)
    }

    @Test
    fun testClear() {
        data.postValue(emptyList())

        verify(observer).onChanged(argThat { it?.isEmpty() ?: false })
    }
}

package version.evening.canvas.flutrack.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MemoryFlutweetsStorageTest {
    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet()

    private lateinit var storage: MemoryFlutweetsStorage

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @Before
    fun setup() {
        storage = MemoryFlutweetsStorage()
        storage.save(listOf(tweet1, tweet2))
    }

    @Test
    fun testSave() {
        val observer = TestObserver<FluTweet>()

        storage.asObservable().subscribe(observer)

        observer.apply {
            assertValueCount(2)
            assertValues(tweet1, tweet2)
            assertComplete()
        }
    }

    @Test
    fun testClear() {
        val observer = TestObserver<FluTweet>()

        storage.apply {
            clear()
            asObservable().subscribe(observer)
        }

        observer.apply {
            assertValueCount(0)
            assertComplete()
        }
    }
}

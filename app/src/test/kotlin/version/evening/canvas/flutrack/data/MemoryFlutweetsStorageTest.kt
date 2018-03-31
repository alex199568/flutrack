package version.evening.canvas.flutrack.data

import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class MemoryFlutweetsStorageTest {
    private val tweet1 = FluTweet()
    private val tweet2 = FluTweet()

    private lateinit var storage: MemoryFlutweetsStorage

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

package version.evening.canvas.flutrack.data

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable

class MemoryFlutweetsStorage {
    private val tweets = mutableListOf<FluTweet>()

    val data = MutableLiveData<List<FluTweet>>()

    fun rewrite(tweets: List<FluTweet>) {
        clear()
        save(tweets)
    }

    fun save(tweets: List<FluTweet>) {
        this.tweets.addAll(tweets)
        data.postValue(tweets)
    }

    fun asObservable(): Observable<FluTweet> = Observable.fromIterable(tweets)

    fun clear() {
        tweets.clear()
        data.postValue(tweets)
    }
}

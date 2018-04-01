package version.evening.canvas.flutrack.data

import io.reactivex.Observable

class MemoryFlutweetsStorage {
    private val tweets = mutableListOf<FluTweet>()

    fun save(tweets: List<FluTweet>) {
        this.tweets.addAll(tweets)
    }

    fun asObservable(): Observable<FluTweet> = Observable.fromIterable(tweets)

    fun clear() {
        tweets.clear()
    }
}

package version.evening.canvas.flutrack.data

import io.reactivex.Observable

class MemoryFlutweetsStorage {
    private val tweets = mutableListOf<FluTweet>()

    fun rewrite(tweets: List<FluTweet>) {
        this.tweets.clear()
        save(tweets)
    }

    fun save(tweets: List<FluTweet>) {
        this.tweets.addAll(tweets)
    }

    fun asObservable(): Observable<FluTweet> = Observable.fromIterable(tweets)

    fun clear() {
        tweets.clear()
    }
}

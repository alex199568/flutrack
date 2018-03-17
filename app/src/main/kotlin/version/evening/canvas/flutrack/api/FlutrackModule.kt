package version.evening.canvas.flutrack.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import version.evening.canvas.flutrack.AppScope
import java.io.File
import java.util.concurrent.TimeUnit

const val FLUTRACK_ALL_BASE_URL = "http://flutrack.org"
const val CONNECT_TIMEOUT = 60L
const val READ_TIMEOUT = 300L
const val MAX_CACHE_SIZE = 1024L * 1024L * 16L

@Module
class FlutrackModule {
    @AppScope
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .baseUrl(FLUTRACK_ALL_BASE_URL)
                    .build()

    @AppScope
    @Provides
    fun provideFlutrackAll(retrofit: Retrofit): FlutrackAll =
            retrofit.create(FlutrackAll::class.java)

    @AppScope
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @AppScope
    @Provides
    fun provideCache(context: Context): Cache {
        val directory = File(context.cacheDir, "responses")
        return Cache(directory, MAX_CACHE_SIZE)
    }

    @AppScope
    @Provides
    fun provideClient(cache: Cache): OkHttpClient =
            OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .build()
}

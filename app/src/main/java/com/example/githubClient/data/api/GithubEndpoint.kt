package com.example.githubClient.data.api
import android.content.Context
import androidx.lifecycle.Observer
import com.example.githubClient.common.Constants
import com.example.githubClient.core.GithubApp
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.network.NetworkStatusLiveData
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch

/**
 * A singleton object that provides a Github API service, allowing only one ongoing network call at a time.
 */
object GithubEndpoint {

    // Create a custom Dispatcher with a max request limit of 1
    private val dispatcher = Dispatcher().apply { maxRequests = 1 }

    // Network connected latch to wait for network connection before making a network call
    private var networkConnectedLatch = CountDownLatch(1)

    // Create an OkHttpClient instance with an interceptor for adding the Authorization header,
    // and configure it to use the custom Dispatcher with a max request limit of 1
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // Wait for the network to become available before continuing the call
            networkConnectedLatch.await()

            val originalRequest = chain.request()
            val requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "token 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")
                .build()
            chain.proceed(requestWithToken)
        }
        .dispatcher(dispatcher)
        .build()

    // Create a Retrofit instance with the specified base URL and client,
    // and add a converter factory for JSON serialization/deserialization using Gson
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.GITHUB_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * A GithubApi service instance created using the configured Retrofit instance.
     */
    val githubApi: GithubApi = retrofit.create(GithubApi::class.java)

    fun setupNetworkListener(context: Context) {
        val networkStatusLiveData = NetworkStatusLiveData(context)
        networkStatusLiveData.observeForever { networkStatus ->
            when (networkStatus) {
                NetworkStatus.CONNECTED    -> {
                    networkConnectedLatch.countDown()
                }

                NetworkStatus.DISCONNECTED -> {
                    networkConnectedLatch = CountDownLatch(1)
                }
            }
        }
    }
}

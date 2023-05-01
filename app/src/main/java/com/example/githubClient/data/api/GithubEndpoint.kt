package com.example.githubClient.data.api

import android.content.Context
import androidx.lifecycle.Observer
import com.example.githubClient.common.Constants
import com.example.githubClient.core.GithubApp
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.network.NetworkStatusLiveData
import kotlinx.coroutines.delay
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.math.pow

/**
 * A singleton object that provides a Github API service, allowing only one ongoing network call at a time.
 */
object GithubEndpoint {

    // exponential back-off initializers
    // max retry on any type of network problem
    private const val maxRetries = 3
    private const val initialBackoffMillis = 1000L

    // current back off power by backoffFactor creates the next back off period
    private const val backoffFactor = 2.0

    // Create a custom Dispatcher with a max request limit of 1
    private val dispatcher = Dispatcher().apply { maxRequests = 1 }

    // Network connected latch to wait for network connection before making a network call
    private var networkConnectedLatch = CountDownLatch(1)

    // Create an OkHttpClient instance with an interceptor for adding the Authorization header,
    // and configure it to use the custom Dispatcher with a max request limit of 1
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            var attempt = 0
            var response: Response? = null
            val exception: IOException? = null
            while (attempt < maxRetries && (response == null || !response.isSuccessful)) {
                // Wait for the network to become available before continuing the call
                networkConnectedLatch.await()
                try {
                    val originalRequest = chain.request()
                    val requestWithToken = originalRequest.newBuilder()
                        .header("Authorization", "token ${Constants.GITHUB_API_KEY}")
                        .build()
                    response = chain.proceed(requestWithToken)
                }catch (e: IOException){
                    exception?.initCause(e)
                }
                if (response == null || !response.isSuccessful) {
                    response?.close()
                    attempt++
                    val backoffTime = initialBackoffMillis * backoffFactor.pow(attempt - 1).toLong()
                    Thread.sleep(backoffTime)
                }
            }
            response ?: throw exception
                ?: IOException("ExponentialBackoffInterceptor: Max attempts reached without success")
        }
        .retryOnConnectionFailure(true)
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

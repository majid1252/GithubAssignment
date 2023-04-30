package com.example.githubClient.data.api
import com.example.githubClient.common.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GithubEndpoint {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "token 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")
                .build()
            chain.proceed(requestWithToken)
        }
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.GITHUB_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubApi: GithubApi = retrofit.create(GithubApi::class.java)
}
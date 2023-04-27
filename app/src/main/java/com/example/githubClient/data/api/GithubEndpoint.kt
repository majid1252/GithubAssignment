package com.example.githubClient.data.api
import GithubApi
import com.example.githubClient.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GithubEndpoint {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.GITHUB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubApi: GithubApi = retrofit.create(GithubApi::class.java)
}
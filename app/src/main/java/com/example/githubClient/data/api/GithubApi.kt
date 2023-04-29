package com.example.githubClient.data.api

import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubDetailedUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("users")
    suspend fun getUsers(@Query("since") since: Int): Response<List<GithubBaseUser>>

    @GET("users/{username}")
    fun getUserInfo(@Path("username") username: String?): Call<GithubDetailedUser?>?
}
package com.example.githubClient.data

import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.api.GithubEndpoint
import com.example.githubClient.data.model.GithubDetailedUser
import com.example.githubClient.data.model.IGithubBaseUser
import org.junit.Assert.assertTrue
import org.junit.Test

class GithubApiTest {
    private val api: GithubApi = GithubEndpoint.githubApi

    @Test
    fun `print API response with input`() {
        // Call API
        val response = api.getUsers(0).execute()

        // Check if the response is successful
        assertTrue(response.isSuccessful)

        // Print API response
        val users: List<IGithubBaseUser>? = response.body()
        users?.forEach { user ->
            println("User: $user\n")
        }
    }

    @Test
    fun `print API response with input for detailed user`() {
        // Call API
        val response = api.getUserInfo("mojombo")?.execute()

        // Check if the response is successful
        assertTrue(response?.isSuccessful == true)

        // Print API response
        val userInfo: GithubDetailedUser? = response?.body()
        println("UserInfo: $userInfo\n")
    }
}
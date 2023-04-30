package com.example.githubClient.data.repo

import GithubUsersRemoteMediator
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserWithLocalData
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class) class GithubUsersRepository(
    private val githubApiService: GithubApi,
    private val githubDatabase: GithubDatabase
) {

    private val githubRemoteMediator = GithubUsersRemoteMediator(githubApiService, githubDatabase)

    // Variable to store the determined page size
    private var initialPageSize: Int? = null

    // Function to fetch the initial data and set the page size
    suspend fun fetchInitialData(): Int {
        val response = githubApiService.getUsers(since = 0)
        initialPageSize = if (response.isSuccessful) {
            val users = response.body() ?: emptyList()
            githubDatabase.githubUserDao.insertAll(users)
            users.size
        } else {
            // Handle error or set a default value
            30
        }
        return initialPageSize!!
    }

    fun getUsers(): Flow<PagingData<GithubUserWithLocalData>> {
        val pageSize = initialPageSize ?: 30
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true
            ),
            remoteMediator = githubRemoteMediator,
            pagingSourceFactory = { githubDatabase.githubUserDao.getUsersWithLocalData() }
        ).flow
    }
}
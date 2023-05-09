package com.example.githubClient.data.repo

import GithubUsersRemoteMediator
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubClient.common.Constants
import com.example.githubClient.common.SharedPreferenceManager
import com.example.githubClient.core.GithubApp
import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.api.GithubEndpoint
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubDetailedUser
import com.example.githubClient.data.model.GithubUserLocalData
import com.example.githubClient.data.model.GithubUserWithLocalData
import com.example.githubClient.viewModel.GithubUsersViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
* A singleton repository object for managing GitHub users data.
*
* This object encapsulates fetching and storing of GitHub users data from both the API
* and local Room database, along with handling pagination using the Paging library.
*/
@OptIn(ExperimentalPagingApi::class) object GithubUsersRepository {

    private val githubApiService: GithubApi = GithubEndpoint.githubApi
    private val githubDatabase: GithubDatabase = GithubDatabase.getInstance(GithubApp.getContext())
    private val sharedPreferences = SharedPreferenceManager.sharedPreferences
    private val githubRemoteMediator = GithubUsersRemoteMediator(githubApiService, githubDatabase)

    /**
     * Fetches the paginated GitHub users data and returns it as a Flow of PagingData.
     *
     * @return [Flow] of [PagingData] of [GithubUserWithLocalData] objects.
     */
    suspend fun getUsers(): Flow<PagingData<GithubUserWithLocalData>> {
        return Pager(
            config = PagingConfig(
                pageSize = getPageSize(),
                enablePlaceholders = true,
            ),
            remoteMediator = githubRemoteMediator,
            pagingSourceFactory = { githubDatabase.githubUserDao.getUsersWithLocalData() }
        ).flow
    }

    /**
     * Retrieves the size of the page used for pagination from shared preferences or the API.
     *
     * If the page size is already stored in the shared preferences, it returns the stored value.
     * Otherwise, it fetches the size of the first page of users from the API, stores it in shared preferences,
     * and returns the fetched value. If the API call fails, it returns the default page size.
     *
     * @return An [Int] representing the page size for pagination.
     */
    private suspend fun getPageSize(): Int {
        return if (sharedPreferences.contains(Constants.PAGE_SIZE_KEY))
            sharedPreferences.getInt(Constants.PAGE_SIZE_KEY, GithubUsersViewModel.CONSTANTS_DEFAULT_PAGE_SIZE)
        else
            githubApiService.getUsers(0).body()?.size?.let {
                sharedPreferences.edit().putInt(Constants.PAGE_SIZE_KEY, it).apply()
                it
            } ?: GithubUsersViewModel.CONSTANTS_DEFAULT_PAGE_SIZE
    }

    suspend fun searchForUsers(query: String) = githubDatabase.githubUserDao.queryUsersByUsernameAndNote(query)
    suspend fun fetchUserDetails(username: String) = githubApiService.getUserInfo(username)
    suspend fun getUserDetails(username: String) = githubDatabase.githubUserDao.getUserDetails(username)
    suspend fun getLocalUserData(id: Int) = githubDatabase.githubUserDao.getLocalUserData(id)
    suspend fun updateUserDetails(userDetails: GithubDetailedUser) = githubDatabase.githubUserDao.insertUserDetails(userDetails)
    suspend fun updateUserNote(userLocalData: GithubUserLocalData) = githubDatabase.githubUserDao.insertLocalUserData(userLocalData)
    suspend fun deleteUserNote(userLocalData: GithubUserLocalData) = githubDatabase.githubUserDao.deleteLocalUserData(userLocalData)
}
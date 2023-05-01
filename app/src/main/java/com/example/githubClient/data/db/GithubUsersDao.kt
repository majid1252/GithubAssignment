package com.example.githubClient.data.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserLocalData
import com.example.githubClient.data.model.GithubUserWithLocalData

@Dao
interface GithubUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubBaseUser>)

    @Query("SELECT * FROM github_users")
    fun getUsers(): PagingSource<Int, GithubBaseUser>

    @Transaction
    @Query("SELECT * FROM github_users")
    fun getUsersWithLocalData(): PagingSource<Int, GithubUserWithLocalData>

    @Transaction
    @Query("SELECT * FROM github_users WHERE login LIKE :query")
    suspend fun queryUsers(query:String): List<GithubUserWithLocalData>

    @Query("DELETE FROM github_users")
    suspend fun clearAll()

    // Methods for handling UserLocalData like notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalUserData(localUserData: GithubUserLocalData)

    @Query("SELECT * FROM local_user_data WHERE userId = :userId")
    suspend fun getLocalUserData(userId: Int): GithubUserLocalData?

//    @Query("UPDATE github_users SET githubUserLocalData = :localUserData WHERE id = :userId")
//    suspend fun updateLocalUserData(userId: Int, localUserData: GithubUserLocalData)
}
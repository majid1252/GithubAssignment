package com.example.githubClient.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubDetailedUser
import com.example.githubClient.data.model.GithubUserLocalData
import com.example.githubClient.data.model.GithubUserWithLocalData

@Dao
interface GithubUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubBaseUser>)

    @Query("SELECT * FROM github_users")
    fun getUsers(): PagingSource<Int, GithubBaseUser>

    @Query("SELECT * FROM github_users")
    fun getUsersRaw(): List<GithubBaseUser>

    @Transaction
    @Query("SELECT * FROM github_users")
    fun getUsersWithLocalData(): PagingSource<Int, GithubUserWithLocalData>

    @Transaction
    @Query("SELECT * FROM github_users")
    fun getUsersWithLocalDataRaw():List<GithubUserWithLocalData>

    @Transaction
    @Query("SELECT * FROM github_users LEFT JOIN local_user_data ON github_users.id = local_user_data.userId " +
               "WHERE github_users.login GLOB :query " +
               "OR local_user_data.note GLOB :query")
    suspend fun queryUsersByUsernameAndNote(query:String): List<GithubUserWithLocalData>

    @Query("DELETE FROM github_users")
    suspend fun clearAll()

    // Methods for handling UserLocalData like notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalUserData(localUserData: GithubUserLocalData)

    // Delete user local data
    @Delete
    suspend fun deleteLocalUserData(localUserData: GithubUserLocalData)

    @Query("SELECT * FROM local_user_data WHERE userId = :userId")
    suspend fun getLocalUserData(userId: Int): GithubUserLocalData?

    @Query("UPDATE local_user_data SET note = :localUserData WHERE userId = :userId")
    suspend fun updateLocalUserData(userId: Int, localUserData: String)

    // Methods for handling user details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetails(userDetails: GithubDetailedUser)

    @Query("SELECT * FROM detailed_user_data WHERE login = :username")
    suspend fun getUserDetails(username: String): GithubDetailedUser?

}
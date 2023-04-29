package com.example.githubClient.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubClient.data.model.GithubBaseUser

@Dao
interface GithubUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubBaseUser>)

    @Query("SELECT * FROM github_users")
    fun getUsers(): PagingSource<Int, GithubBaseUser>

    @Query("DELETE FROM github_users")
    suspend fun clearAll()
}
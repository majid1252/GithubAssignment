package com.example.githubClient.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "local_user_data",
    foreignKeys = [
        ForeignKey(
            entity = GithubBaseUser::class,
            parentColumns = ["id"],
            childColumns = ["userId"]
        )
    ]
)
data class GithubUserLocalData(
    @PrimaryKey
    val userId: String,
    val note: String?
)
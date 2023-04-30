package com.example.githubClient.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserLocalData

data class GithubUserWithLocalData(
    @Embedded val githubUser: GithubBaseUser,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val localData: GithubUserLocalData?
)
package com.example.githubClient.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "github_users")
data class GithubBaseUser(
    @PrimaryKey @SerializedName("id") override val id: Int,
    override val login: String,
    override val node_id: String,
    override val avatar_url: String,
    override val gravatar_id: String,
    override val url: String,
    override val html_url: String,
    override val followers_url: String,
    override val following_url: String,
    override val gists_url: String,
    override val starred_url: String,
    override val subscriptions_url: String,
    override val organizations_url: String,
    override val repos_url: String,
    override val events_url: String,
    override val received_events_url: String,
    override val type: String,
    override val site_admin: Boolean
    ) : IGithubBaseUser
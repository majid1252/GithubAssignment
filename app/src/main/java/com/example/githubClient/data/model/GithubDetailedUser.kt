package com.example.githubClient.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailed_user_data")
data class GithubDetailedUser(
    @PrimaryKey
    override val login: String,
    override val id: Int,
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
    override val site_admin: Boolean,
    override val name: String?,
    override val company: String?,
    override val blog: String?,
    override val location: String?,
    override val email: String?,
    override val hireable: String?,
    override val bio: String?,
    override val twitter_username: String?,
    override val public_repos: Int,
    override val public_gists: Int,
    override val followers: Int,
    override val following: Int,
    override val created_at: String,
    override val updated_at: String): IGithubBaseUser, IGithubBserUserProperty

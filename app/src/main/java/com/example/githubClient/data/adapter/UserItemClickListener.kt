package com.example.githubClient.data.adapter

import com.example.githubClient.data.model.GithubBaseUser

/**
 * Interface for handling user item click
 */
interface UserItemClickListener {
    fun onUserClicked(user: GithubBaseUser)
}
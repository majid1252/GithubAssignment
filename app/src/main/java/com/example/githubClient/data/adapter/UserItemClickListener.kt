package com.example.githubClient.data.adapter

import androidx.appcompat.widget.AppCompatImageView
import com.example.githubClient.data.model.GithubBaseUser

/**
 * Interface for handling user item click
 */
interface UserItemClickListener {
    fun onUserClicked(user: GithubBaseUser, avatar: AppCompatImageView)
}
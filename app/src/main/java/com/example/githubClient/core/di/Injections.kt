package com.example.githubClient.core.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.githubClient.data.api.GithubApi
import com.example.githubClient.data.api.GithubEndpoint
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.repo.GithubUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class) @Module object Injections {

    @Provides
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun providesResources(context: Context): Resources {
        return context.resources
    }

    private fun provideGithubRepository(context: Context): GithubUsersRepository {
        return GithubUsersRepository(GithubEndpoint.githubApi, GithubDatabase.getInstance(context))
    }

}

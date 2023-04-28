package com.example.githubClient.core.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class) @Module object SingletonModules {

    @Provides
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun providesResources(context: Context): Resources {
        return context.resources
    }

}

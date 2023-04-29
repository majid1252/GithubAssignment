package com.example.githubClient.core

import android.app.Application
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GithubApp : Application() {
    companion object {
        private lateinit var instance: GithubApp
        fun getContext() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Mavericks.initialize(this)
        //Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
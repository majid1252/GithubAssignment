package com.example.githubClient.common

import android.content.Context
import android.content.SharedPreferences
import com.example.githubClient.core.GithubApp

/**
 * A singleton object for managing shared preferences in the application.
 *
 * This object encapsulates access to shared preferences and provides utility methods for
 * storing and retrieving various data types.
 *
 * @property context The application context used to access shared preferences.
 * @property sharedPreferences The shared preferences instance.
 */
object SharedPreferenceManager {

    val sharedPreferences: SharedPreferences
        get() = GithubApp.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private const val PREFERENCES_NAME = Constants.SHARED_PREFERENCES_NAME

}

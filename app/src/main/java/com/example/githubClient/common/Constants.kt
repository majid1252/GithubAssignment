package com.example.githubClient.common

/**
 * Constants used throughout the app.
 */
object Constants {
    const val GITHUB_API_KEY: String = "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"

    // Endpoints
    const val GITHUB_BASE_URL = "https://api.github.com/"

    // Glide values
    const val GLIDE_CACHE_SIZE = 1024 * 1024 * 100 // 100 MB
    const val GLIDE_CACHE_DIR_NAME = "glide_disk_cache"

    // Shared Preferences
    const val SHARED_PREFERENCES_NAME = ".githubClient"
    const val PAGE_SIZE_KEY = ".pageSizeKey"
}
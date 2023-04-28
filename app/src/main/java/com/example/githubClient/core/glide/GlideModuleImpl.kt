package com.example.githubClient.core.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.example.githubClient.common.Constants

/**
 * A custom Glide module that configures disk caching settings for the Glide image loading library.
 *
 * This module sets up a disk cache of a custom size and location to improve performance by
 * caching images on disk and reducing the need for network requests.
 *
 * @property diskCacheSizeBytes The size of the disk cache in bytes. Defaults to 100 MB.
 * @property diskCachePath The path for the disk cache relative to the application's cache directory. Defaults to "glide_disk_cache".
 *
 * Usage:
 * ```
 * // Load an image using Glide with the custom Glide module configuration
 * GlideApp.with(context)
 *     .load(imageUrl)
 *     .apply(RequestOptions().override(300, 300).centerCrop())
 *     .into(imageView)
 * ```
 */
@GlideModule
class GlideModuleImpl: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Customize Glide settings here
        val diskCacheSizeBytes = Constants.GLIDE_CACHE_SIZE
        val diskCachePath = Constants.GLIDE_CACHE_DIR_NAME

        builder.setDiskCache(DiskLruCacheFactory(diskCachePath, diskCacheSizeBytes.toLong()))
    }
}
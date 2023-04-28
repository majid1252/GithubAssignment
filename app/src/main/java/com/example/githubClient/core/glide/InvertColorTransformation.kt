package com.example.githubClient.core.glide

import android.graphics.Bitmap
import android.graphics.Color
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * A custom Glide transformation that inverts the colors of an image.
 *
 * This transformation iterates through all the pixels in the input bitmap and inverts
 * their RGB colors.
 *
 * Usage:
 * ```
 * GlideApp.with(context)
 *     .asBitmap()
 *     .load(imageUrl)
 *     .transition(BitmapTransitionOptions.withCrossFade())
 *     .transform(InvertColorsTransformation())
 *     .into(imageView)
 * ```
 */
class InvertColorTransformation : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val idBytes = ID.toByteArray(Charsets.UTF_8)
        messageDigest.update(idBytes)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // define the size of the bitmap
        val width = toTransform.width
        val height = toTransform.height

        // create a bitmap with the defined size as output
        val result = pool.get(width, height, Bitmap.Config.ARGB_8888)

        // simply iterating through the pixels and invert them by subtracting from 255
        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = toTransform.getPixel(x, y)
                val r = 255 - Color.red(pixel)
                val g = 255 - Color.green(pixel)
                val b = 255 - Color.blue(pixel)
                val invertedPixel = Color.argb(Color.alpha(pixel), r, g, b)
                result.setPixel(x, y, invertedPixel)
            }
        }
        return result
    }

    // A unique id for this transformation that can be used by the Glide library to cache the
    companion object {
        private const val ID = "com.example.myapplication.glide.com.example.githubClient.core.glide.InvertColorsTransformation"
        private val ID_BYTES = ID.toByteArray(Charsets.UTF_8)
    }
}

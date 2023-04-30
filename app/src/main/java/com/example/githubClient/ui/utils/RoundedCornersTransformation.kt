package com.example.githubClient.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * A custom Glide {@link BitmapTransformation} that applies rounded corners to an image.
 * <p>
 * Usage:
 * <pre>
 * Glide.with(context)
 *     .load(imageUrl)
 *     .apply(RequestOptions().transform(com.example.githubClient.ui.utils.RoundedCornersTransformation(cornerRadius)))
 *     .into(imageView)
 * </pre>
 */
class RoundedCornersTransformation(private val radius: Float) : BitmapTransformation() {

    /**
     * Transforms the given {@link Bitmap} by applying rounded corners with the specified radius.
     *
     * @param pool The {@link BitmapPool} to obtain a new {@link Bitmap} if needed.
     * @param toTransform The {@link Bitmap} to be transformed.
     * @param outWidth The target width of the transformed bitmap.
     * @param outHeight The target height of the transformed bitmap.
     * @return The transformed {@link Bitmap} with rounded corners.
     */
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(outWidth, outHeight, toTransform.config)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        val rectF = RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)

        return bitmap
    }

    /**
     * Updates the {@link MessageDigest} with a unique key representing the transformation.
     *
     * @param messageDigest The {@link MessageDigest} to be updated.
     */
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(("com.example.githubClient.ui.utils.RoundedCornersTransformation-$radius").toByteArray())
    }
}

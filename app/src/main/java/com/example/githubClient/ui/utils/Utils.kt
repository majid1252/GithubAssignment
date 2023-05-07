package com.example.githubClient.ui.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator

fun View.slideIn() {
    val animator = ObjectAnimator.ofFloat(this, "translationY", this.height.toFloat(), 0f)
    animator.duration = 300
    animator.interpolator = AnticipateOvershootInterpolator(2f)
    animator.start()
}

fun View.slideOut() {
    val animator = ObjectAnimator.ofFloat(this, "translationY", 0f, this.height.toFloat())
    animator.duration = 300
    animator.interpolator = AnticipateOvershootInterpolator(2f)
    animator.start()
}

fun Int.formatDescriptiveCounter(): String {
    return when {
        this >= 1_000_000 -> "%.1fM".format(this / 1_000_000.0)
        this >= 1_000 -> "%.1fK".format(this / 1_000.0)
        else -> this.toString()
    }
}
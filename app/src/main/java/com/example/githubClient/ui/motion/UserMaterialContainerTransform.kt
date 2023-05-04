package com.example.githubClient.ui.motion

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.githubClient.R
import com.example.githubClient.ui.theme.md_theme_light_secondary
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

object AppMotions{
    fun createUserDetailTransition(context:Context): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            duration = 300L
            scrimColor = ContextCompat.getColor(context, android.R.color.transparent)
            setAllContainerColors(ContextCompat.getColor(context, androidx.constraintlayout.widget.R.color.material_deep_teal_500))
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            interpolator = FastOutSlowInInterpolator()
            setPathMotion(MaterialArcMotion())
        }
    }
}


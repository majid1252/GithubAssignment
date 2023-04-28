package com.example.githubClient.common

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import javax.inject.Inject

class StringProvider @Inject constructor(private val resources: Resources) {

    fun getString(@StringRes resId: Int): String {
        return resources.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return resources.getString(resId, *formatArgs)
    }

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any?): String {
        return resources.getQuantityString(resId, quantity, *formatArgs)
    }
}

package com.keylesspalace.tusky.util

import android.content.Context
import androidx.core.content.getSystemService

/**
 * Similar to [Context.getSystemService] but throws an exception if the service is not found.
 */
inline fun <reified T : Any> Context.requireSystemService(): T {
    return getSystemService<T>() ?: error("System service type ${this::class.java.canonicalName} not found.")
}

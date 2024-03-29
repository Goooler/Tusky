package com.keylesspalace.tusky.util

import android.content.SharedPreferences

fun SharedPreferences.requireString(key: String, defValue: String = ""): String {
    return getString(key, defValue) ?: defValue
}

package com.jay.wjshare.utils

import android.util.Log
import com.jay.wjshare.BuildConfig

fun makeLog(simpleName: String, message: String) {
    if (BuildConfig.DEBUG) Log.d(simpleName, message)
}
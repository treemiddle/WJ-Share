package com.jay.wjshare.utils

import android.net.Uri
import com.jay.wjshare.BuildConfig

fun getUri(): Uri {
    return Uri.Builder()
            .scheme("https")
            .authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .appendQueryParameter("scope", "repo,user")
            .build()
}
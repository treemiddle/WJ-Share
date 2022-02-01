package com.jay.wjshare.data.local.source

import com.jay.wjshare.data.local.prefs.PrefsHelper
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    private val prefs: PrefsHelper
) : AuthLocalDataSource {

    override var accessToken: String
        get() = prefs.accessToken
        set(value) {
            prefs.accessToken = value
        }

}
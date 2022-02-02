package com.jay.wjshare.data.local.prefs

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class PrefsHelperImpl @Inject constructor(applicationContext: Context) : PrefsHelper {

    private val prefs = applicationContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    override var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, null) ?: ""
        @Synchronized
        set(value) {
            prefs.edit {
                putString(ACCESS_TOKEN, value)
            }
        }

    override fun clear() = prefs.edit {
        remove(ACCESS_TOKEN)
            .apply()
    }


    companion object {
        const val FILE_NAME = "wj_style_share"

        private const val ACCESS_TOKEN = "access_token"
    }
}
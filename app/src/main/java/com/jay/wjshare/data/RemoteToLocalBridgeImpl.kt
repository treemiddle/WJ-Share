package com.jay.wjshare.data

import com.jay.wjshare.data.local.source.AuthLocalDataSource
import javax.inject.Inject

class RemoteToLocalBridgeImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : RemoteToLocalBridge {

    override val accessToken: String
        get() = authLocalDataSource.accessToken

}
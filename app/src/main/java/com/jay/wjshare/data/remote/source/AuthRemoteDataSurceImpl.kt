package com.jay.wjshare.data.remote.source

import com.jay.wjshare.data.remote.api.AuthApi
import io.reactivex.Single
import javax.inject.Inject

class AuthRemoteDataSurceImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRemoteDataSource {

    override fun requestAccessToken(code: String): Single<String> {
        return authApi.requestLogin(code = code)
            .map { it.access_token }
    }

}
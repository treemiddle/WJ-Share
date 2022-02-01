package com.jay.wjshare.data.remote.source

import io.reactivex.Single

interface AuthRemoteDataSource {

    fun requestAccessToken(code: String): Single<String>

}
package com.jay.wjshare.domain.repository

import io.reactivex.Single

interface AuthRepository {

    val accessToken: String

    fun requestAccessToken(code: String): Single<String>

}
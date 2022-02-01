package com.jay.wjshare.data

import com.jay.wjshare.data.local.source.AuthLocalDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override val accessToken: String
        get() = authLocalDataSource.accessToken

    override fun requestAccessToken(code: String): Single<String> {
        return authRemoteDataSource.requestAccessToken(code)
            .flatMap {
                authLocalDataSource.accessToken = it
                Single.just(it)
            }
    }

}
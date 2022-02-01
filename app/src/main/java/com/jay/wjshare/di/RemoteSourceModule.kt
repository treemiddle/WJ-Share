package com.jay.wjshare.di

import com.jay.wjshare.data.remote.api.AuthApi
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSurceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteSourceModule {

    @Provides
    @Singleton
    fun provideAuthRemoteSource(authApi: AuthApi): AuthRemoteDataSource {
        return AuthRemoteDataSurceImpl(authApi)
    }

}
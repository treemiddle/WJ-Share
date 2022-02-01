package com.jay.wjshare.di

import com.jay.wjshare.data.AuthRepositoryImpl
import com.jay.wjshare.data.local.source.AuthLocalDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSurceImpl
import com.jay.wjshare.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authLocalDataSource: AuthLocalDataSource,
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(
            authLocalDataSource,
            authRemoteDataSource
        )
    }

}
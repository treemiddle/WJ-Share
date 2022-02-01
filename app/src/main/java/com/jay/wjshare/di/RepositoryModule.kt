package com.jay.wjshare.di

import com.jay.wjshare.RemoteToLocalBridgeImpl
import com.jay.wjshare.data.AuthRepositoryImpl
import com.jay.wjshare.data.GitHubRepositoryImpl
import com.jay.wjshare.data.RemoteToLocalBridge
import com.jay.wjshare.data.local.source.AuthLocalDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSurceImpl
import com.jay.wjshare.data.remote.source.GitHubRemoteDataSourceImpl
import com.jay.wjshare.data.remote.source.GithubRemoteDataSource
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.domain.repository.GitHubRepository
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

    @Provides
    @Singleton
    fun provideRemoteToLocalBridge(
        authLocalDataSource: AuthLocalDataSource
    ): RemoteToLocalBridge {
        return RemoteToLocalBridgeImpl(authLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(
        githubRemoteDataSource: GithubRemoteDataSource
    ): GitHubRepository {
        return GitHubRepositoryImpl(githubRemoteDataSource)
    }

}
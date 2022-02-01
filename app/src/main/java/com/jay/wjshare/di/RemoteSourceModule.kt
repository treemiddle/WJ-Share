package com.jay.wjshare.di

import com.jay.wjshare.data.remote.api.AuthApi
import com.jay.wjshare.data.remote.api.GitHubApi
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSurceImpl
import com.jay.wjshare.data.remote.source.GitHubRemoteDataSourceImpl
import com.jay.wjshare.data.remote.source.GithubRemoteDataSource
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

    @Provides
    @Singleton
    fun provideGitHubRemoteSource(gitHubApi: GitHubApi): GithubRemoteDataSource {
        return GitHubRemoteDataSourceImpl(gitHubApi)
    }

}
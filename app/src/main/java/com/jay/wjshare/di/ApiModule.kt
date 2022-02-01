package com.jay.wjshare.di

import com.jay.wjshare.data.remote.api.AuthApi
import com.jay.wjshare.data.remote.api.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(@NetworkModule.AuthRetrofit retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubApi(@NetworkModule.GitHubRetrofit retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }

}
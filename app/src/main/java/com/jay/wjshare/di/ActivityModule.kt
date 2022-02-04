package com.jay.wjshare.di

import com.jay.wjshare.ui.main.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class Search

@Qualifier
annotation class Profile

@Module
@InstallIn(SingletonComponent::class)
object ActivityModule {

    @Provides
    @Singleton
    @Search
    fun provideSearchAdapter(): RepositoryAdapter {
        return RepositoryAdapter()
    }

    @Provides
    @Singleton
    @Profile
    fun provideProfileAdapter(): RepositoryAdapter {
        return RepositoryAdapter()
    }

}
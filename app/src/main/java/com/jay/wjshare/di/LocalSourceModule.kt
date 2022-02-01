package com.jay.wjshare.di

import com.jay.wjshare.data.local.prefs.PrefsHelper
import com.jay.wjshare.data.local.source.AuthLocalDataSource
import com.jay.wjshare.data.local.source.AuthLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalSourceModule {

    @Provides
    @Singleton
    fun provideAuthLocalSource(prefs: PrefsHelper): AuthLocalDataSource {
        return AuthLocalDataSourceImpl(prefs)
    }

}
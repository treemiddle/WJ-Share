package com.jay.wjshare.di

import com.jay.wjshare.ui.navigator.MainNavigator
import com.jay.wjshare.ui.navigator.MainNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorModule {

    @Binds
    abstract fun bindMainNavigator(impl: MainNavigatorImpl): MainNavigator

}
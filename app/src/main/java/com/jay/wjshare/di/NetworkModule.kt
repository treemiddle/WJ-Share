package com.jay.wjshare.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jay.wjshare.BuildConfig
import com.jay.wjshare.data.RemoteToLocalBridge
import com.jay.wjshare.data.remote.interceptor.WJInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GitHubRetrofit

    @AuthRetrofit
    @Provides
    @Singleton
    fun provideAuthRetrofit(bridge: RemoteToLocalBridge): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL)
            .client(provideOkHttp(bridge))
            .addCallAdapterFactory(provideRxAdapter())
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .build()
    }

    @GitHubRetrofit
    @Provides
    @Singleton
    fun provideGitHubRetrofit(bridge: RemoteToLocalBridge): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_BASE_URL)
            .client(provideOkHttp(bridge))
            .addCallAdapterFactory(provideRxAdapter())
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .build()
    }

    @Provides
    @Singleton
    fun provideRxAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(bridge: RemoteToLocalBridge): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideOkHttpLogging())
            .addInterceptor(provideInterceptor(bridge))
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(bridge: RemoteToLocalBridge): WJInterceptor {
        return WJInterceptor(bridge)
    }

}
package com.jay.wjshare.data.remote.interceptor

import com.jay.wjshare.BuildConfig
import com.jay.wjshare.data.RemoteToLocalBridge
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class WJInterceptor @Inject constructor(
    private val bridge: RemoteToLocalBridge
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = bridge.accessToken
        val request = chain.request().newBuilder()

        if (token.isEmpty()) {
            request.addHeader(BuildConfig.ACCEPT, BuildConfig.ACCEPT_VALUE)
        } else {
            request.addHeader(BuildConfig.ACCEPT, BuildConfig.ACCEPT_VALUE)
                .addHeader(BuildConfig.AUTHORIZATION, token)
        }

        return chain.proceed(request.build())
    }
}
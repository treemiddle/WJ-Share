package com.jay.wjshare.data.remote.api

import com.jay.wjshare.BuildConfig
import com.jay.wjshare.data.remote.model.auth.AuthResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("access_token")
    fun requestLogin(
        @Field("client_id") id: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") key: String = BuildConfig.CLIENT_KEY,
        @Field("code") code: String
    ): Single<AuthResponse>

}
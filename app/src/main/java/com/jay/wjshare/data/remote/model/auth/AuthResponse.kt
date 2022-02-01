package com.jay.wjshare.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token")
    val access_token: String,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("token_type")
    val token_type: String
)
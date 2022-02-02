package com.jay.wjshare.data.remote.api

import com.jay.wjshare.data.remote.model.profile.ProfileResponseItem
import com.jay.wjshare.data.remote.model.profile.MyInfoResponse
import com.jay.wjshare.data.remote.model.repositories.RepositoriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("search/repositories")
    fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Single<RepositoriesResponse>

    @GET("users/{username}/repos")
    fun getMyRepositories(
        @Path("username") username: String
    ): Single<List<ProfileResponseItem>>

    @GET("user")
    fun getMyInfo(): Single<MyInfoResponse>

}
package com.jay.wjshare.data.remote.api

import com.jay.wjshare.data.remote.model.repositories.RepositoriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    /**
     * 소유자 이름  -> onwer,login
     * 레파지토리 이름  -> name
     * 레파지토리 설명 -> description
     * 스타 개수 stargazers_count
     * 스타 버튼
     */
    @GET("repositories")
    fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Single<RepositoriesResponse>

}
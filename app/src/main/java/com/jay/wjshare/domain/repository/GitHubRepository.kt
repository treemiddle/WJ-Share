package com.jay.wjshare.domain.repository

import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.domain.model.DomainMyInfoModel
import com.jay.wjshare.domain.model.DomainMyRepoModel
import io.reactivex.Single

interface GitHubRepository {

    fun getRepositories(query: String, page: Int): Single<List<DomainGithubModel>>

    fun getMyRepositories(userName: String): Single<List<DomainMyRepoModel>>

    fun getMyInfo(userName: String): Single<DomainMyInfoModel>

}
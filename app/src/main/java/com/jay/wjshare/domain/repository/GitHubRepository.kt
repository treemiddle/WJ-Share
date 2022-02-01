package com.jay.wjshare.domain.repository

import com.jay.wjshare.domain.model.DomainGithubModel
import io.reactivex.Single

interface GitHubRepository {

    fun getRepositories(query: String, page: Int): Single<List<DomainGithubModel>>

}
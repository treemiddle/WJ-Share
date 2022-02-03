package com.jay.wjshare.data

import com.jay.wjshare.data.mapper.GitHubDataMapper
import com.jay.wjshare.data.mapper.mapToDomain
import com.jay.wjshare.data.remote.source.GithubRemoteDataSource
import com.jay.wjshare.domain.model.DomainGithubModel
import com.jay.wjshare.domain.model.DomainMyInfoModel
import com.jay.wjshare.domain.model.DomainMyRepoModel
import com.jay.wjshare.domain.repository.GitHubRepository
import io.reactivex.Single
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val githubRemoteDataSource: GithubRemoteDataSource
) : GitHubRepository {

    override fun getRepositories(query: String, page: Int): Single<List<DomainGithubModel>> {
        return githubRemoteDataSource.getRepositories(query, page)
            .map { it.map(GitHubDataMapper::mapToDomain) }
    }

    override fun getMyInfo(): Single<Pair<DomainMyInfoModel, List<DomainMyRepoModel>>> {
        return githubRemoteDataSource.getMyInfo()
            .map { it.mapToDomain() }
            .flatMap { getMyRepositories(it) }
    }

    private fun getMyRepositories(
        model: DomainMyInfoModel
    ): Single<Pair<DomainMyInfoModel, List<DomainMyRepoModel>>> {
        return githubRemoteDataSource.getMyRepositories(model.userName)
            .map { it.mapToDomain() }
            .map { list -> model to list }
    }

}
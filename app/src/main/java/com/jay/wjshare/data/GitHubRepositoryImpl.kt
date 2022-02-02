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

    override fun getMyRepositories(userName: String): Single<List<DomainMyRepoModel>> {
        return githubRemoteDataSource.getMyRepositories(userName)
            .map { it.mapToDomain()}
    }

    override fun getMyInfo(): Single<DomainMyInfoModel> {
        return githubRemoteDataSource.getMyInfo()
            .map { it.mapToDomain() }
    }

}
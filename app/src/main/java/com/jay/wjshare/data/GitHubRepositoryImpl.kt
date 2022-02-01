package com.jay.wjshare.data

import com.jay.wjshare.data.mapper.GitHubDataMapper
import com.jay.wjshare.data.remote.source.GithubRemoteDataSource
import com.jay.wjshare.domain.model.DomainGithubModel
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

}
package com.jay.wjshare.data.remote.source

import com.jay.wjshare.data.model.MyInfoModel
import com.jay.wjshare.data.model.MyRepositoryModel
import com.jay.wjshare.data.model.RepositoriesModel
import com.jay.wjshare.data.remote.api.GitHubApi
import com.jay.wjshare.data.remote.mapper.GitHubMapper
import com.jay.wjshare.data.remote.mapper.mapToData
import io.reactivex.Single
import javax.inject.Inject

class GitHubRemoteDataSourceImpl @Inject constructor(
    private val gitHubApi: GitHubApi
) : GithubRemoteDataSource {

    override fun getRepositories(query: String, page: Int): Single<List<RepositoriesModel>> {
        return gitHubApi.getRepositories(query, page)
            .map { it.items.map(GitHubMapper::mapToData) }
    }

    override fun getMyRepositories(userName: String): Single<List<MyRepositoryModel>> {
        return gitHubApi.getMyRepositories(userName)
            .map { it.mapToData() }
    }

    override fun getMyInfo(userName: String): Single<MyInfoModel> {
        return gitHubApi.getMyInfo(userName)
            .map { it.mapToData() }
    }

}
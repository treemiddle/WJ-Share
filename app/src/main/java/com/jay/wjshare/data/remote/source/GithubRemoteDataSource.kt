package com.jay.wjshare.data.remote.source

import com.jay.wjshare.data.model.RepositoriesModel
import io.reactivex.Single

interface GithubRemoteDataSource {

    fun getRepositories(query: String, page: Int): Single<List<RepositoriesModel>>

}
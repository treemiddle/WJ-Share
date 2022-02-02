package com.jay.wjshare.data.remote.source

import com.jay.wjshare.data.model.MyInfoModel
import com.jay.wjshare.data.model.MyRepositoryModel
import com.jay.wjshare.data.model.RepositoriesModel
import io.reactivex.Single

interface GithubRemoteDataSource {

    fun getRepositories(query: String, page: Int): Single<List<RepositoriesModel>>

    fun getMyRepositories(userName: String): Single<List<MyRepositoryModel>>

    fun getMyInfo(userName: String): Single<MyInfoModel>

}
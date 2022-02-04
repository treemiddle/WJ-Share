package com.jay.wjshare.ui.main

import com.jay.wjshare.ui.model.RepoModel
import io.reactivex.subjects.BehaviorSubject

interface LikeRepository {

    val copyRepoSubject: BehaviorSubject<RepoModel>

    fun copyRepository(repo: RepoModel): RepoModel

    fun getCurrentRepositoryIndex(repo: RepoModel, list: List<RepoModel>): Int

    fun newSubmitList(repo: RepoModel, oldList: List<RepoModel>?): List<RepoModel>

    fun copyRepoOnNext(newRepo: RepoModel)

    fun findRepository(newRepo: RepoModel, list: List<RepoModel>?): Boolean

    fun updateRepository(newRepo: RepoModel)

}
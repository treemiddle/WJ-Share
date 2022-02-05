package com.jay.wjshare.ui.main

import androidx.lifecycle.LiveData
import com.jay.wjshare.ui.model.RepoModel
import io.reactivex.subjects.BehaviorSubject

interface RepositoryListener {

    val repositories: LiveData<List<RepoModel>>
    val hasLikedRepo: LiveData<RepoModel>
    val sharedList: LiveData<List<RepoModel>>
    val copyRepositorySubect: BehaviorSubject<RepoModel>

    fun setHasLikedRepository(repo: RepoModel)

    fun applyCopyRepositoryToList(list: List<RepoModel>?)

    fun applyRepositoryToList(list: List<RepoModel>?)

    fun setSharedList(list: List<RepoModel>)

    fun getSharedList(): List<RepoModel>

    fun copyRepositoryOnNext(repo: RepoModel)

}
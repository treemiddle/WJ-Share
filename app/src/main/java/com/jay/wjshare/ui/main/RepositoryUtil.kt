package com.jay.wjshare.ui.main

import com.jay.wjshare.ui.mapper.applyClick
import com.jay.wjshare.ui.model.RepoModel
import io.reactivex.subjects.Subject


fun <T> newList(list: List<T>?): MutableList<T> {
    return mutableListOf<T>().apply { list?.let { addAll(it) } }
}

fun copyRepository(repo: RepoModel, subject: Subject<RepoModel>) =
    repo.copy(hasLiked = repo.hasLiked.not()).applyClick(subject)

fun getIndex(repo: RepoModel, list: List<RepoModel>) =
    list.indexOf(repo)

fun checkResultOfList(repo: RepoModel, list: List<RepoModel>?): Boolean {
    list?.let {
        val repoModel = it.find { model -> model.id == repo.id }

        return repoModel != null
    } ?: return false
}

fun findRepositoryModel(list: List<RepoModel>, repo: RepoModel): RepoModel? {
    return list.find { it.id == repo.id }
}

fun submitList(
    list: List<RepoModel>,
    sharedList: List<RepoModel>?
) : List<RepoModel> {
    val newList = newList(list)

    sharedList?.let {
        for (i in it.indices) {
            val repoModel = findRepositoryModel(newList, it[i])

            repoModel?.let { repo ->
                val index = getIndex(repo, newList)

                if (index != -1) {
                    newList[index] = it[i]
                }
            }
        }
    }

    return newList
}

fun RepoModel.copyRepositoryToList(subject: Subject<RepoModel>, list: List<RepoModel>?): List<RepoModel>? {
    val newRepo = this.applyClick(subject)

    return if (checkResultOfList(newRepo, list)) {
        val repoList = newList(list)
        val index = getIndex(newRepo.copy(hasLiked = newRepo.hasLiked.not()), repoList)

        if (index != -1) {
            repoList[index] = newRepo
        }

        repoList
    } else {
        null
    }
}

fun RepoModel.repositoryToList(
    subject: Subject<RepoModel>,
    list: List<RepoModel>?,
    listener: RepositoryListener
): List<RepoModel> {
    val repoList = newList(list)
    val newRepo = copyRepository(this, subject)
    val index = getIndex(this, repoList)

    if (index != -1) {
        repoList[index] = newRepo
        listener.setHasLikedRepository(newRepo)
    }

    return repoList
}
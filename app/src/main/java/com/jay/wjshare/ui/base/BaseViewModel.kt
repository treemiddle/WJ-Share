package com.jay.wjshare.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jay.wjshare.ui.mapper.applyClick
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.makeLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable by lazy(::CompositeDisposable)
    protected val repoClick = PublishSubject.create<RepoModel>()
    protected val sharedRepo = BehaviorSubject.create<RepoModel>()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _sharedRepoList = MutableLiveData<List<RepoModel>>()
    val sharedRepoList: LiveData<List<RepoModel>>
        get() = _sharedRepoList

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected fun saveRepository(repo: RepoModel) = sharedRepo.onNext(repo)

    protected fun uploadRepository(repo: RepoModel, oldList: List<RepoModel>?) : List<RepoModel>{
        val newList = mutableListOf<RepoModel>().apply {
            addAll(oldList!!)
        }
        val newRepo = copyRepository(repo)
        val index = getCurrentIndex(repo, newList)
        newList[index] = newRepo

        return newList
    }

    private fun copyRepository(repo: RepoModel): RepoModel {
        return repo.copy(hasLiked = repo.hasLiked.not()).applyClick(repoClick)
    }

    private fun getCurrentIndex(repo: RepoModel, newList: List<RepoModel>): Int {
        return newList.indexOf(repo)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}
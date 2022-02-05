package com.jay.wjshare.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.main.RepositoryListener
import com.jay.wjshare.ui.main.copyRepositoryToList
import com.jay.wjshare.ui.main.repositoryToList
import com.jay.wjshare.ui.main.submitList
import com.jay.wjshare.ui.mapper.mapToPresentation
import com.jay.wjshare.ui.model.MyInfoModel
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : BaseViewModel(), RepositoryListener {

    override val copyRepositorySubect: BehaviorSubject<RepoModel> = BehaviorSubject.create()

    private val _repositories = MutableLiveData<List<RepoModel>>()
    override val repositories: LiveData<List<RepoModel>>
        get() = _repositories

    private val _myInfo = MutableLiveData<MyInfoModel>()
    val myInfo: LiveData<MyInfoModel>
        get() = _myInfo

    private val _hasLikedRepo = MutableLiveData<RepoModel>()
    override val hasLikedRepo: LiveData<RepoModel>
        get() = _hasLikedRepo

    private val _sharedList = MutableLiveData<List<RepoModel>>()
    override val sharedList: LiveData<List<RepoModel>>
        get() = _sharedList

    init {
        getMyInfo()
        bindRx()
    }

    override fun setHasLikedRepository(repo: RepoModel) {
        _hasLikedRepo.value = repo
    }

    override fun applyCopyRepositoryToList(list: List<RepoModel>?) {
        list?.let { _repositories.value = it }
    }

    override fun applyRepositoryToList(list: List<RepoModel>?) {
        list?.let { _repositories.value = it }
    }

    override fun setSharedList(list: List<RepoModel>) {
        _sharedList.value = list
    }

    override fun getSharedList(): List<RepoModel> = _sharedList.value ?: emptyList()

    override fun copyRepositoryOnNext(repo: RepoModel) = copyRepositorySubect.onNext(repo)

    private fun bindRx() {
        compositeDisposable.addAll(
            repoClickSubject.subscribe { repo ->
                applyRepositoryToList(
                    list = repo.repositoryToList(
                        subject = repoClickSubject,
                        list = getRepositories(),
                        listener = this
                    )
                )
            },

            copyRepositorySubect.subscribe { repo ->
                applyCopyRepositoryToList(
                    list = repo.copyRepositoryToList(
                        subject = repoClickSubject,
                        list = getRepositories()
                    )
                )
            }
        )
    }

    private fun getMyInfo() {
        gitHubRepository.getMyInfo()
            .observeOn(Schedulers.computation())
            .map { it.mapToPresentation(repoClickSubject) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ (model, repos) ->
                setMyInfo(model)
                setRepositories(repos)
            }, { t ->
                makeLog(javaClass.simpleName, "info error: ${t.localizedMessage}")
            }).addTo(compositeDisposable)
    }

    private fun setMyInfo(info: MyInfoModel) {
        _myInfo.value = info
    }

    private fun setRepositories(list: List<RepoModel>) {
        _repositories.value = submitList(
            list = list,
            sharedList = getSharedList()
        )
    }

    private fun getRepositories(): List<RepoModel> = _repositories.value ?: emptyList()

}
package com.jay.wjshare.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.main.LikeRepository
import com.jay.wjshare.ui.mapper.applyClick
import com.jay.wjshare.ui.mapper.mapToPresentation
import com.jay.wjshare.ui.model.MyInfoModel
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.newFixedThreadPoolContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : BaseViewModel(), LikeRepository {

    override val copyRepoSubject: BehaviorSubject<RepoModel> = BehaviorSubject.create()

    private val _myRepos = MutableLiveData<List<RepoModel>>()
    val myRepos: LiveData<List<RepoModel>>
        get() = _myRepos

    private val _myInfo = MutableLiveData<MyInfoModel>()
    val myInfo: LiveData<MyInfoModel>
        get() = _myInfo

    private val _hasLikedRepo = MutableLiveData<RepoModel>()
    val hasLikedRepo: LiveData<RepoModel>
        get() = _hasLikedRepo

    init {
        getMyInfo()
        bindRx()
    }

    override fun copyRepository(repo: RepoModel) =
        repo.copy(hasLiked = repo.hasLiked.not()).applyClick(repoClickSubject)

    override fun getCurrentRepositoryIndex(repo: RepoModel, list: List<RepoModel>) =
        list.indexOf(repo)

    override fun newSubmitList(repo: RepoModel, oldList: List<RepoModel>?): List<RepoModel> {
        val newList = mutableListOf<RepoModel>().apply {
            addAll(oldList!!)
        }
        val newRepo = copyRepository(repo)
        val index = getCurrentRepositoryIndex(repo, newList)
        newList[index] = newRepo
        setHasLikedRepository(newRepo)

        return newList
    }

    override fun copyRepoOnNext(newRepo: RepoModel) = copyRepoSubject.onNext(newRepo)

    override fun findRepository(newRepo: RepoModel, list: List<RepoModel>?): Boolean {
        list?.let {
            val repoModel = it.find { repo -> repo.id == newRepo.id }

            return repoModel != null

        } ?: return false
    }

    override fun updateRepository(newRepo: RepoModel) {
        if (findRepository(newRepo, getMyRepositories())) {
            getMyRepositories()?.let {
                val newList = mutableListOf<RepoModel>().apply { addAll(it) }
                val index = getCurrentRepositoryIndex(newRepo.copy(hasLiked = newRepo.hasLiked.not()), newList)

                if (index != -1) {
                    newList[index] = newRepo
                    setMyRepositories(newList)
                }

            } ?: return
        }
    }

    fun setSharedRepos(sharedList: List<RepoModel>) {
        val sameList = sharedList.filter { repo ->
            repo.id in getMyRepositories()!!.map { oldRepo ->
                oldRepo.id
            }
        }

        setLikeRepositoryFromSearch(sameList)
    }

    private fun bindRx() {
        compositeDisposable.addAll(
            repoClickSubject.subscribe { setMyRepositories(newSubmitList(it, getMyRepositories())) },

            copyRepoSubject.subscribe { updateRepository(it.applyClick(repoClickSubject)) }
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
                setMyRepositories(repos)
            }, { t ->
                makeLog(javaClass.simpleName, "info error: ${t.localizedMessage}")
            }).addTo(compositeDisposable)
    }

    private fun setMyRepositories(list: List<RepoModel>) {
        _myRepos.value = list
    }

    private fun getMyRepositories() = _myRepos.value

    private fun setMyInfo(info: MyInfoModel) {
        _myInfo.value = info
    }

    private fun setHasLikedRepository(repo: RepoModel) {
        _hasLikedRepo.value = repo
    }

    private fun setLikeRepositoryFromSearch(likeList: List<RepoModel>) {
        if (likeList.isNotEmpty()) {
            val newList = mutableListOf<RepoModel>().apply { addAll(getMyRepositories()!!) }
            for (i in likeList.indices) {
                val index = getCurrentRepositoryIndex(
                    repo = likeList[i].copy(hasLiked = likeList[i].hasLiked.not()),
                    list = newList
                )

                if (index != -1) {
                    newList[index] = likeList[i]
                }
            }

            setMyRepositories(newList)
        }
    }

}
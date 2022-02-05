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

    private val _sharedList = MutableLiveData<List<RepoModel>>()
    val sharedList: LiveData<List<RepoModel>>
        get() = _sharedList

    init {
        getMyInfo()
        bindRx()
    }

    // (좋아요 레포지토리를 만들기 위함) hasLiked 반대, clickSubject 등록
    override fun copyRepository(repo: RepoModel) =
        repo.copy(hasLiked = repo.hasLiked.not()).applyClick(repoClickSubject)

    // 레포지토리 리스트에서 해당 레포지토리의 인덱스를 찾음
    override fun getCurrentRepositoryIndex(repo: RepoModel, list: List<RepoModel>) =
        list.indexOf(repo)

    // 보여져있는 리스트 좋아요 클릭하면 해당 아이템 찾아서 좋아요 레포지토리 등록/해제 후 리스트 다시 그리기
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

    // 리시버로 들어온 레포지토리 전달 용
    override fun copyRepoOnNext(newRepo: RepoModel) = copyRepoSubject.onNext(newRepo)

    // 레포지토리 리스트에서 전달 받은 아이템을 id값으로 찾는다.(전달 받은 아이템임, hasliked 반대값으로 들어옴)
    override fun findRepository(newRepo: RepoModel, list: List<RepoModel>?): Boolean {
        list?.let {
            val repoModel = it.find { repo -> repo.id == newRepo.id }

            return repoModel != null

        } ?: return false
    }

    // 리시버로 전달받은 아이템을 현재 리스트에서 찾아서 있는지 확인 후 setRepositorys로 그림?
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

//    fun initFirstSharedRepo(list: List<RepoModel>) {
//        _sharedList.value = list
//    }

    fun setSharedList(list: List<RepoModel>) {
        _sharedList.value = list

        //getMyRepositories()?.let { setMyRepositories(it) }

        //setMyRepositories()
//        val sameList = list.filter { repo ->
//            repo.id in getMyRepositories()!!.map { oldRepo ->
//                oldRepo.id
//            }
//        }
//
//        setLikeRepositoryFromSearch(sameList)
    }

    private fun bindRx() {
        compositeDisposable.addAll(
            // 아이템 클릭 subject
            repoClickSubject.subscribe { setMyRepositories(newSubmitList(it, getMyRepositories())) },

            // 리시버에서 넘어온 copy 아이템 subject
            copyRepoSubject.subscribe { updateRepository(it.applyClick(repoClickSubject)) }
        )
    }

    // 내 정보들 불러오기
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

    // 그리는 용도...(api나, 리스트를 다시 그릴 때...)
    private fun setMyRepositories(list: List<RepoModel>) {
        val newList = mutableListOf<RepoModel>().apply { addAll(list) }

        getSharedList()?.let {
            for (i in it.indices) {
                val model = newList.find { n -> n.id == _sharedList.value!![i].id }

                model?.let { m ->
                    val index = getCurrentRepositoryIndex(m, newList)
                    if (index != -1) {
                        newList[index] = _sharedList.value!![i]
                    }
                }
            }
        }

        _myRepos.value = newList
    }

    // 현재 가지고 있는 레포지토리 리스트를 반환
    private fun getMyRepositories() = _myRepos.value

    // 내 정보들 입력
    private fun setMyInfo(info: MyInfoModel) {
        _myInfo.value = info
    }

    // 좋아요 클릭할 때 레포지토리 입력
    private fun setHasLikedRepository(repo: RepoModel) {
        _hasLikedRepo.value = repo
    }

    private fun getSharedList() = _sharedList.value

    // 이건 무슨
//    private fun setLikeRepositoryFromSearch(likeList: List<RepoModel>) {
//        if (likeList.isNotEmpty()) {
//            val newList = mutableListOf<RepoModel>().apply { addAll(getMyRepositories()!!) }
//            for (i in likeList.indices) {
//                val index = getCurrentRepositoryIndex(
//                    repo = likeList[i].copy(hasLiked = likeList[i].hasLiked.not()),
//                    list = newList
//                )
//
//                if (index != -1) {
//                    newList[index] = likeList[i]
//                }
//            }
//
//            setMyRepositories(newList)
//        }
//    }

}
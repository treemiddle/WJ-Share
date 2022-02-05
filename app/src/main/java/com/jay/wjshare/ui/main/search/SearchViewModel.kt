package com.jay.wjshare.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.main.LikeRepository
import com.jay.wjshare.ui.mapper.Mapper
import com.jay.wjshare.ui.mapper.applyClick
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.Event
import com.jay.wjshare.utils.enums.MessageSet
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val authRepository: AuthRepository
) : BaseViewModel(), LikeRepository {

    override val copyRepoSubject: BehaviorSubject<RepoModel> = BehaviorSubject.create()
    private val searchClickSubject = PublishSubject.create<Unit>()
    private val querySubject = BehaviorSubject.createDefault("")

    private val _repositories = MutableLiveData<List<RepoModel>>()
    val repositories: LiveData<List<RepoModel>>
        get() = _repositories

    private val _searchState = MutableLiveData<Event<MessageSet>>()
    val searchState: LiveData<Event<MessageSet>>
        get() = _searchState

    private val _hasLikedRepo = MutableLiveData<RepoModel>()
    val hasLikedRepo: LiveData<RepoModel>
        get() = _hasLikedRepo

    private val _sharedList = MutableLiveData<List<RepoModel>>()
    val sharedList: LiveData<List<RepoModel>>
        get() = _sharedList

    init {
        val button = searchClickSubject.throttleFirst(1, TimeUnit.SECONDS)
            .map { querySubject.value }
        val query = querySubject.debounce(1200, TimeUnit.MILLISECONDS)

        compositeDisposable.addAll(
            Observable.merge(button, query)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showLoading() }
                .switchMapSingle { name ->
                    gitHubRepository.getRepositories(name, 1)
                }
                .observeOn(Schedulers.computation())
                .map {
                    it.map(Mapper::mapToPresentation).map { repo ->
                        repo.applyClick(repoClickSubject)
                    }
                }
                .onErrorReturn { listOf() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { hideLoading() }
                .subscribe { repos -> setRepositorys(repos) },

            // 아이템 클릭 subject
            repoClickSubject.subscribe { setRepositorys(newSubmitList(it, getRepositorys())) },

            // 리시버에서 넘어온 copy 아이템 subject
            copyRepoSubject.subscribe { updateRepository(it.applyClick(repoClickSubject)) }
        )
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
        if (index != -1) {
            newList[index] = newRepo
        }

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
        if (findRepository(newRepo, getRepositorys())) {
            getRepositorys()?.let {
                val newList = mutableListOf<RepoModel>().apply { addAll(it) }
                val index = getCurrentRepositoryIndex(newRepo.copy(hasLiked = newRepo.hasLiked.not()), newList)
                if (index != -1) {
                    newList[index] = newRepo
                    setRepositorys(newList)
                }

            } ?: return
        }
    }

    // 페이징
    fun onLoadMore(page: Int) {
        gitHubRepository.getRepositories(
            query = querySubject.value!!,
            page = page
        )
            .observeOn(Schedulers.computation())
            .map {
                it.map(Mapper::mapToPresentation).map { repo ->
                    repo.applyClick(repoClickSubject)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ repos ->
                if (repos.isNotEmpty()) {
                    setPagingRepositories(repos)
                }
            }, { t ->
                makeLog(javaClass.simpleName, "more error: ${t.localizedMessage}")
            }).addTo(compositeDisposable)
    }

    // 키보드 입력
    fun debounceQuery(query: String) {
        if (getAccessToken().isEmpty()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            querySubject.onNext(query)
        }
    }

    // 검색하기 버튼 클릭
    fun onSearchClick() {
        if (getAccessToken().isEmpty()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            searchClickSubject.onNext(Unit)
        }
    }

    // 메인뷰모델에서 가져온 좋아요된 레포지토리를 저장
    fun setSharedList(list: List<RepoModel>) {
        _sharedList.value = list
    }

    // 그리는 용도...(api나, 리스트를 다시 그릴 때...)
    private fun setRepositorys(list: List<RepoModel>) {
        val newList = mutableListOf<RepoModel>().apply { addAll(list) }

        _sharedList.value?.let {
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

        _repositories.value = newList
    }

    // 현재 가지고 있는 레포지토리 리스트를 반환
    private fun getRepositorys() = _repositories.value

    // 페이징 리스트 입력
    private fun setPagingRepositories(list: List<RepoModel>) {
        val newList = mutableListOf<RepoModel>().apply {
            addAll(getRepositorys()!!)
            addAll(list)
        }

        setRepositorys(newList)
    }

    // 액세스 토큰 가져오기
    private fun getAccessToken() = authRepository.accessToken

    // 현재 state 상태 입력
    private fun setMessageState(state: MessageSet) {
        _searchState.value = Event(state)
    }

    // 좋아요 클릭할 때 레포지토리 입력
    private fun setHasLikedRepository(repo: RepoModel) {
        _hasLikedRepo.value = repo
    }

}
package com.jay.wjshare.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.main.RepositoryListener
import com.jay.wjshare.ui.main.copyRepositoryToList
import com.jay.wjshare.ui.main.repositoryToList
import com.jay.wjshare.ui.main.submitList
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
) : BaseViewModel(), RepositoryListener {

    override val copyRepositorySubect: BehaviorSubject<RepoModel> = BehaviorSubject.create()
    private val searchClickSubject = PublishSubject.create<Unit>()
    private val querySubject = BehaviorSubject.createDefault("")
    private val pageSubject = BehaviorSubject.create<Int>()

    private val _repositories = MutableLiveData<List<RepoModel>>()
    override val repositories: LiveData<List<RepoModel>>
        get() = _repositories

    private val _searchState = MutableLiveData<Event<MessageSet>>()
    val searchState: LiveData<Event<MessageSet>>
        get() = _searchState

    private val _hasLikedRepo = MutableLiveData<RepoModel>()
    override val hasLikedRepo: LiveData<RepoModel>
        get() = _hasLikedRepo

    private val _sharedList = MutableLiveData<List<RepoModel>>()
    override val sharedList: LiveData<List<RepoModel>>
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
                .subscribe { repos ->
                    setRepositories(repos)
                    pageOnNext(1)
                },

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

    fun onLoadMore() {
        pageOnNext(getPage() + 1)

        gitHubRepository.getRepositories(
            query = querySubject.value!!,
            page = getPage()
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

    fun debounceQuery(query: String) {
        if (getAccessToken().isEmpty()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            querySubject.onNext(query)
        }
    }

    fun onSearchClick() {
        if (getAccessToken().isEmpty()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            searchClickSubject.onNext(Unit)
        }
    }

    private fun setPagingRepositories(list: List<RepoModel>) {
        val newList = mutableListOf<RepoModel>().apply {
            addAll(getRepositories())
            addAll(list)
        }

        _repositories.value = newList
    }

    private fun getAccessToken() = authRepository.accessToken

    private fun setMessageState(state: MessageSet) {
        _searchState.value = Event(state)
    }

    private fun setRepositories(list: List<RepoModel>) {
        _repositories.value = submitList(
            list = list,
            sharedList = getSharedList()
        )
    }

    private fun getRepositories(): List<RepoModel> = _repositories.value ?: emptyList()

    private fun pageOnNext(page: Int) = pageSubject.onNext(page)

    private fun getPage() = pageSubject.value!!

}
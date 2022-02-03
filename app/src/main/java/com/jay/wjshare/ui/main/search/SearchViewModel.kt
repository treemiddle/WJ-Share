package com.jay.wjshare.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.mapper.Mapper
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.Event
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
) : BaseViewModel() {

    private val searchClickSubject = PublishSubject.create<Unit>()
    private val querySubject = BehaviorSubject.createDefault("")

    private val _repositories = MutableLiveData<List<RepoModel>>()
    val repositories: LiveData<List<RepoModel>>
        get() = _repositories

    private val _searchState = MutableLiveData<Event<MessageSet>>()
    val searchState: LiveData<Event<MessageSet>>
        get() = _searchState

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
                .map { it.map(Mapper::mapToPresentation) }
                .onErrorReturn { listOf() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { hideLoading() }
                .subscribe { repos -> setRepositorys(repos) }
        )
    }

    fun onLoadMore(query: String, page: Int) {
        gitHubRepository.getRepositories(
            query = querySubject.value!!,
            page = page
        )
            .observeOn(Schedulers.computation())
            .map { it.map(Mapper::mapToPresentation) }
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
        if (getAccessToken()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            querySubject.onNext(query)
        }
    }

    fun onSearchClick() {
        if (getAccessToken()) {
            setMessageState(MessageSet.EMPTY_TOKEN)
        } else {
            searchClickSubject.onNext(Unit)
        }
    }

    private fun setRepositorys(list: List<RepoModel>) {
        _repositories.value = list
    }

    private fun setPagingRepositories(list: List<RepoModel>) {
        val newList = mutableListOf<RepoModel>().apply {
            addAll(repositories.value!!)
            addAll(list)
        }

        setRepositorys(newList)
    }

    private fun getAccessToken() = authRepository.accessToken.isEmpty()

    private fun setMessageState(state: MessageSet) {
        _searchState.value = Event(state)
    }

    enum class MessageSet {
        EMPTY_TOKEN
    }

}
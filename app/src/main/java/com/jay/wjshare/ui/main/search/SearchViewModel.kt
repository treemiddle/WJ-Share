package com.jay.wjshare.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.main.search.mapper.SearchMapper
import com.jay.wjshare.ui.main.search.model.RepoModel
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository
) : BaseViewModel() {

    private val _repositories = MutableLiveData<List<RepoModel>>()
    val repositories: LiveData<List<RepoModel>>
        get() = _repositories

    fun onSearchClick() {
        gitHubRepository.getRepositories("studymovie", 1)
            .observeOn(Schedulers.computation())
            .map { it.map(SearchMapper::mapToPresentation) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({
                setRepositorys(it)
            }, { t ->
                makeLog(javaClass.simpleName, "repositories error: ${t.localizedMessage}")
            }).addTo(compositeDisposable)
    }

    private fun setRepositorys(list: List<RepoModel>) {
        _repositories.value = list
    }

}
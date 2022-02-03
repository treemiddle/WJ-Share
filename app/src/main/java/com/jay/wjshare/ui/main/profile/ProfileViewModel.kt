package com.jay.wjshare.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.GitHubRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.mapper.mapToPresentation
import com.jay.wjshare.ui.model.MyInfoModel
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.addHeaderLenient
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    gitHubRepository: GitHubRepository
) : BaseViewModel() {

    private val _myRepos = MutableLiveData<List<RepoModel>>()
    val myRepos: LiveData<List<RepoModel>>
        get() = _myRepos

    private val _myInfo = MutableLiveData<MyInfoModel>()
    val myInfo: LiveData<MyInfoModel>
        get() = _myInfo

    init {
        gitHubRepository.getMyInfo()
            .observeOn(Schedulers.computation())
            .map { it.mapToPresentation() }
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

    private fun setMyInfo(info: MyInfoModel) {
        _myInfo.value = info
    }

}
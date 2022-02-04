package com.jay.wjshare.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.Event
import com.jay.wjshare.utils.enums.ScreenType
import com.jay.wjshare.utils.getUri
import com.jay.wjshare.utils.makeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _bottomNaviType = MutableLiveData<ScreenType>()
    val bottomNaviType: LiveData<ScreenType>
        get() = _bottomNaviType

    private val _loginState = MutableLiveData(false)
    val loginState: LiveData<Boolean>
        get() = _loginState

    private val _loginUrl = MutableLiveData<Event<Uri>>()
    val loginUrl: LiveData<Event<Uri>>
        get() = _loginUrl

    private val sharedRepoList = mutableListOf<RepoModel>()

    init {
        // 테스트 위함 토큰 지우기
        authRepository.clear()
    }

    fun onLoginStateClick() = when (getLoginState()) {
        true -> {
            setLoginState(false)
            clearToken()
        }
        else -> requestLogin()
    }

    fun onBottomNavigationListener(screenType: ScreenType) {
        _bottomNaviType.value = screenType
    }

    fun getAccessToken() = authRepository.accessToken

    fun requestAccessToken(code: String) {
        authRepository.requestAccessToken(code)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setLoginState(true)
                toProfileFragment()
            }, { t ->
                makeLog(javaClass.simpleName, "error: ${t.localizedMessage}")
            }).addTo(compositeDisposable)
    }

    fun setSharedRepositoryFromSearch(repoModel: RepoModel) {
        val repo = sharedRepoList.find { it.id == repoModel.id }

        if (repo == null) {
            sharedRepoList.add(repoModel)
        } else {
            val index = sharedRepoList.indexOf(repoModel.copy(hasLiked = repoModel.hasLiked.not()))
            if (index != -1) {
                sharedRepoList.removeAt(index)
            }
        }
    }

    fun getSharedRepositoryFromSearch() = sharedRepoList

    private fun getBottomNaviType() = _bottomNaviType.value

    private fun setLoginState(state: Boolean) {
        _loginState.value = state
    }

    private fun getLoginState() = _loginState.value

    private fun requestLogin() {
        _loginUrl.value = Event(getUri())
    }

    private fun clearToken() {
        authRepository.clear()
        toProfileFragment()
    }

    private fun toProfileFragment() {
        if (getBottomNaviType() == ScreenType.PROFILE) {
            onBottomNavigationListener(ScreenType.PROFILE)
        }
    }

}
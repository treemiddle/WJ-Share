package com.jay.wjshare.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.utils.Event
import com.jay.wjshare.utils.ScreenType
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

    init {
        // 테스트를 위해 들어올 때 마다 토큰 삭제
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
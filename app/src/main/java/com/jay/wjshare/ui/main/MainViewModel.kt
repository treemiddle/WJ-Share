package com.jay.wjshare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.utils.ScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    private val githubId = BehaviorSubject.createDefault("깃헙아이디")
    private val githubPassword = BehaviorSubject.createDefault("깃헙패스워드")

    private val _bottomNaviType = MutableLiveData<ScreenType>()
    val bottomNaviType: LiveData<ScreenType>
        get() = _bottomNaviType

    private val _loginState = MutableLiveData(false)
    val loginState: LiveData<Boolean>
        get() = _loginState

    fun onLoginStateClick() = when (getLoginState()) {
        true -> setLogout()
        else -> setLogin()
    }

    fun onBottomNavigationListener(screenType: ScreenType) {
        _bottomNaviType.value = screenType
    }

    private fun setLogin() {
        _loginState.value = true
    }

    private fun setLogout() {
        _loginState.value = false
    }

    private fun getLoginState() = _loginState.value

}
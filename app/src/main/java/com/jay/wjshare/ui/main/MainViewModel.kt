package com.jay.wjshare.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.BuildConfig
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.ui.base.BaseViewModel
import com.jay.wjshare.utils.Event
import com.jay.wjshare.utils.ScreenType
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

    private val _code = MutableLiveData<String?>()
    val code: LiveData<String?>
        get() = _code

    fun onLoginStateClick() = when (getLoginState()) {
        true -> {
            setLoginState(false)
            setCode(null)
        }
        else -> requestLogin()
    }

    fun onBottomNavigationListener(screenType: ScreenType) {
        _bottomNaviType.value = screenType
    }

    fun getLoginUrl() = _loginUrl.value

    fun setCode(code: String?) {
        _code.value = code
    }

    fun getCode() = _code.value

    fun requestAccessToken() {
        getCode()?.let {
            authRepository.requestAccessToken(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setLoginState(true)
                }, { t ->
                    makeLog(javaClass.simpleName, "error: ${t.localizedMessage}")
                }).addTo(compositeDisposable)
        }
    }

    private fun setLoginState(state: Boolean) {
        _loginState.value = state
    }

    private fun getLoginState() = _loginState.value

    private fun requestLogin() {
        _loginUrl.value = Event(
            Uri.Builder()
                .scheme("https")
                .authority("github.com")
                .appendPath("login")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
                .build()
        )
    }

}
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

    // 로그인 버튼 클릭
    fun onLoginButtonClick() = when (getLoginState()) {
        true -> {
            setLoginState(false)
            clearToken()
        }
        else -> requestLogin()
    }

    // 바텀네비게이션 타입 클릭
    fun onBottomNavigationClick(screenType: ScreenType) {
        _bottomNaviType.value = screenType
    }

    // 액세스 토큰 가져오기
    fun getAccessToken() = authRepository.accessToken

    // 액세스 토큰 요청
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

    // 좋아요 클릭한 레포지토리 저장
    fun saveSharedRepository(repoModel: RepoModel) {
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

    // 좋아요 클릭된 레포지토리 리스트 반환
    fun getSharedRepositories() = sharedRepoList

    // 바텀네비게이션 타입 반환
    private fun getBottomNaviType() = _bottomNaviType.value

    // 로그인 상태 입력
    private fun setLoginState(state: Boolean) {
        _loginState.value = state
    }

    // 로그인 상태 반환
    private fun getLoginState() = _loginState.value

    // Uri Builder 가져와서 github callback 요청
    private fun requestLogin() {
        _loginUrl.value = Event(getUri())
    }

    // 로그아웃 버튼
    private fun clearToken() {
        authRepository.clear()
        toProfileFragment()
    }

    // 프로필 타입일 때 로그아웃 시 로그인 화면 띄워주기
    private fun toProfileFragment() {
        if (getBottomNaviType() == ScreenType.PROFILE) {
            onBottomNavigationClick(ScreenType.PROFILE)
        }
    }

}
package com.jay.wjshare.ui.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jay.wjshare.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {

    private val _loginState = MutableLiveData<String>()
    val loginState: LiveData<String>
        get() = _loginState

    fun setLoginTitle(state: Boolean) {
        if (state) {

        }
    }

}
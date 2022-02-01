package com.jay.wjshare.ui.main.login

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentLoginBinding
import com.jay.wjshare.ui.base.BaseFragment
import com.jay.wjshare.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment
    : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun setupBinding() {
        binding.vm = viewModel
        binding.avm = activityViewModel
    }

    override fun setupObserving() {

    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

}
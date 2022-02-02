package com.jay.wjshare.ui.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentProfileBinding
import com.jay.wjshare.di.Profile
import com.jay.wjshare.ui.base.BaseFragment
import com.jay.wjshare.ui.main.search.SearchRepoAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment
    : BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by viewModels()
    @Inject
    @Profile
    lateinit var adapter: SearchRepoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUserRepo.adapter = adapter
    }

    override fun setupBinding() {
        binding.vm = viewModel
    }

    override fun setupObserving() {

    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

}
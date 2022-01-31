package com.jay.wjshare.ui.main.search

import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentSearchBinding
import com.jay.wjshare.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    override val viewModel: SearchViewModel by viewModels()

    override fun setupBinding() {
        binding.vm = viewModel
    }

    override fun setupObserving() {

    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

}
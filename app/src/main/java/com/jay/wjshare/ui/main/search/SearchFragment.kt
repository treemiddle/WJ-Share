package com.jay.wjshare.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentSearchBinding
import com.jay.wjshare.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    override val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchRepoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    override fun setupBinding() {
        binding.vm = viewModel
    }

    override fun setupObserving() {

    }

    private fun initAdapter() = with(binding) {
        adapter = SearchRepoAdapter()
        rvSearchRepo.adapter = adapter
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

}
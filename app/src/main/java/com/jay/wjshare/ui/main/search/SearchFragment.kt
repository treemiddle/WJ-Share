package com.jay.wjshare.ui.main.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentSearchBinding
import com.jay.wjshare.di.Search
import com.jay.wjshare.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    override val viewModel: SearchViewModel by viewModels()
    @Inject
    @Search
    lateinit var adapter: SearchRepoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearchRepo.adapter = adapter
    }

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
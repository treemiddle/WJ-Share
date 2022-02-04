package com.jay.wjshare.ui.main.search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentSearchBinding
import com.jay.wjshare.di.Search
import com.jay.wjshare.ui.base.BaseFragment
import com.jay.wjshare.ui.main.MainViewModel
import com.jay.wjshare.ui.main.RepositoryAdapter
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.*
import com.jay.wjshare.utils.enums.MessageSet
import com.jay.wjshare.utils.ext.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {

    override val viewModel: SearchViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    @Inject
    @Search
    lateinit var adapter: RepositoryAdapter

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val copyRepo = intent?.extras?.getParcelable<RepoModel>(REPO_MODEL)
            copyRepo?.let { viewModel.copyRepoOnNext(it) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val filter = IntentFilter().apply { addAction(ACTION_TO_SEARCH) }
        context.registerReceiver(receiver, filter)
    }

    override fun onDetach() {
        context?.unregisterReceiver(receiver)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearchRepo.adapter = adapter
    }

    override fun setupBinding() {
        binding.vm = viewModel
    }

    override fun setupObserving() {
        with(viewModel) {
            searchState.observe(viewLifecycleOwner, EventObserver {
                when (it) {
                    MessageSet.EMPTY_TOKEN ->
                        binding.root.showSnackbar(getString(R.string.need_login_txt), Gravity.TOP)
                }
            })
            hasLikedRepo.observe(viewLifecycleOwner, { repo ->
                activityViewModel.setSharedRepositoryFromSearch(repo)
                val intent = Intent().apply {
                    action = ACTION_TO_PROFILE
                    putExtra(REPO_MODEL, repo)
                }
                context?.sendBroadcast(intent)
            })
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

}
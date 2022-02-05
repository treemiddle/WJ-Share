package com.jay.wjshare.ui.main.search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
import com.jay.wjshare.utils.ACTION_TO_PROFILE
import com.jay.wjshare.utils.ACTION_TO_SEARCH
import com.jay.wjshare.utils.EventObserver
import com.jay.wjshare.utils.REPO_MODEL
import com.jay.wjshare.utils.enums.MessageSet
import com.jay.wjshare.utils.ext.addReceiver
import com.jay.wjshare.utils.ext.removeReceiver
import com.jay.wjshare.utils.ext.sendReceiverMessage
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
            copyRepo?.let { viewModel.copyRepositoryOnNext(it) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        context.addReceiver(
            receiver = receiver,
            action = ACTION_TO_SEARCH
        )
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
                actionByHasLiked(repo)
            })
        }
    }

    override fun actionByHasLiked(repo: RepoModel) {
        activityViewModel.saveSharedRepository(repo)
        context?.sendReceiverMessage(
            Intent().apply {
                action = ACTION_TO_PROFILE
                putExtra(REPO_MODEL, repo)
            }
        )
    }

    override fun onDetach() {
        context?.removeReceiver(receiver)
        super.onDetach()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden && activityViewModel.getSharedRepositories().size != 0) {
            viewModel.setSharedList(activityViewModel.getSharedRepositories())
        }
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

}
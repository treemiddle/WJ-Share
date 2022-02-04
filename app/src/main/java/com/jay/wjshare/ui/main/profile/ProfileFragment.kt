package com.jay.wjshare.ui.main.profile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jay.wjshare.R
import com.jay.wjshare.databinding.FragmentProfileBinding
import com.jay.wjshare.di.Profile
import com.jay.wjshare.ui.base.BaseFragment
import com.jay.wjshare.ui.main.MainViewModel
import com.jay.wjshare.ui.main.RepositoryAdapter
import com.jay.wjshare.ui.model.RepoModel
import com.jay.wjshare.utils.ACTION_TO_PROFILE
import com.jay.wjshare.utils.ACTION_TO_SEARCH
import com.jay.wjshare.utils.REPO_MODEL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment
    : BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    @Inject
    @Profile
    lateinit var adapter: RepositoryAdapter

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val copyRepo = intent?.extras?.getParcelable<RepoModel>(REPO_MODEL)
            copyRepo?.let { viewModel.copyRepoOnNext(it) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val filter = IntentFilter().apply { addAction(ACTION_TO_PROFILE) }
        context.registerReceiver(receiver, filter)
    }

    override fun onDetach() {
        context?.unregisterReceiver(receiver)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvUserRepo.adapter = adapter
    }

    override fun setupBinding() {
        binding.vm = viewModel
    }

    override fun setupObserving() {
        viewModel.hasLikedRepo.observe(viewLifecycleOwner, { repo ->
            val intent = Intent().apply {
                action = ACTION_TO_SEARCH
                putExtra(REPO_MODEL, repo)
            }
            context?.sendBroadcast(intent)
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden && activityViewModel.getSharedRepositoryFromSearch().size != 0) {
            viewModel.setSharedRepos(activityViewModel.getSharedRepositoryFromSearch())
        }
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

}
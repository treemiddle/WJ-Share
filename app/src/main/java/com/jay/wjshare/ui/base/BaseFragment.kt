package com.jay.wjshare.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.jay.wjshare.ui.model.RepoModel

abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel>(
    @LayoutRes
    private val layoutResId: Int
) : Fragment() {

    protected abstract val viewModel: VM
    protected lateinit var binding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { lifecycleOwner = this@BaseFragment }

        setupBinding()
        setupObserving()
    }

    abstract fun setupBinding()

    abstract fun setupObserving()

    protected open fun actionByHasLiked(repo: RepoModel) {

    }

}
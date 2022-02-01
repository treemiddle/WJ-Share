package com.jay.wjshare.utils.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.wjshare.ui.base.BaseListAdapter

@BindingAdapter("setRepositories")
fun <T> RecyclerView.bindRepositories(repos: List<T>?) {
    if (this.adapter is BaseListAdapter<*>) {
        val adapter = this.adapter as BaseListAdapter<T>
        adapter.submitList(repos)
    }
}
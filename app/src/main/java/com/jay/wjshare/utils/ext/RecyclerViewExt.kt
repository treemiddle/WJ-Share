package com.jay.wjshare.utils.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jay.wjshare.ui.base.BaseListAdapter
import com.jay.wjshare.utils.EndlessRecyclerViewScrollListener

@BindingAdapter("setRepositories")
fun <T> RecyclerView.bindRepositories(repos: List<T>?) {
    itemAnimator = null

    if (this.adapter is BaseListAdapter<*>) {
        val adapter = this.adapter as BaseListAdapter<T>
        adapter.submitList(repos)
    }
}

@BindingAdapter("onLoadMore")
fun RecyclerView.bindLoadMore(listener: OnLoadMoreListener) {
    addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
            listener.onLoadMore(page + 1)
        }
    })
}

interface OnLoadMoreListener {
    fun onLoadMore(page: Int)
}
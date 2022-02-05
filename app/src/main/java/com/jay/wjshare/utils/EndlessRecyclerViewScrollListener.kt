package com.jay.wjshare.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotalItemCount: Int = 0
    private var isLoading: Boolean = true

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        lastVisibleItemPositions.indices.forEach {
            if (it == 0)
                maxSize = lastVisibleItemPositions[it]
            else if (lastVisibleItemPositions[it] > maxSize)
                maxSize = lastVisibleItemPositions[it]
        }
        return maxSize
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int? {
        return when (recyclerView.layoutManager) {
            is LinearLayoutManager -> (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            is GridLayoutManager -> (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> getLastVisibleItem(
                (recyclerView.labelFor as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                    null
                )
            )
            else -> null
        }
    }

    private fun getTotalItemCount(recyclerView: RecyclerView): Int? {
        return when (recyclerView.layoutManager) {
            is LinearLayoutManager -> (recyclerView.layoutManager as LinearLayoutManager).itemCount
            is GridLayoutManager -> (recyclerView.layoutManager as GridLayoutManager).itemCount
            is StaggeredGridLayoutManager -> (recyclerView.layoutManager as StaggeredGridLayoutManager).itemCount
            else -> null
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = getTotalItemCount(recyclerView) ?: return
        val lastVisibleItemPosition = getLastVisibleItemPosition(recyclerView) ?: return

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount
            this.isLoading = (totalItemCount == 0)
        }

        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            this.isLoading = false
            this.previousTotalItemCount = totalItemCount
        }

        if (!isLoading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
            this.onLoadMore(recyclerView)
            this.isLoading = true
        }
    }

    abstract fun onLoadMore(view: RecyclerView)

    companion object {
        const val VISIBLE_THRESHOLD = 5
    }

}

//abstract class EndlessRecyclerViewScrollListener(
//    private val layoutManager: RecyclerView.LayoutManager
//) : RecyclerView.OnScrollListener() {
//
//    private var visibleThreshold = 5
//    private var currentPage = 0
//    private var previousTotalItemCount = 0
//    private var loading = true
//    private val startingPageIndex = 0
//
//    init {
//        visibleThreshold = when (layoutManager) {
//            is GridLayoutManager -> visibleThreshold * layoutManager.spanCount
//            is StaggeredGridLayoutManager -> visibleThreshold * layoutManager.spanCount
//            else -> visibleThreshold
//        }
//    }
//
//    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
//        var maxSize = 0
//        for (i in lastVisibleItemPositions.indices) {
//            if (i == 0) {
//                maxSize = lastVisibleItemPositions[i]
//            } else if (lastVisibleItemPositions[i] > maxSize) {
//                maxSize = lastVisibleItemPositions[i]
//            }
//        }
//        return maxSize
//    }
//
//    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
//        val totalItemCount = layoutManager.itemCount
//
//        val lastVisibleItemPosition = when (layoutManager) {
//            is StaggeredGridLayoutManager ->
//                getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null))
//            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
//            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
//            else -> 0
//        }
//
//        if (totalItemCount < previousTotalItemCount) {
//            this.currentPage = this.startingPageIndex
//            this.previousTotalItemCount = totalItemCount
//            if (totalItemCount == 0) {
//                this.loading = true
//            }
//        }
//
//        if (loading && totalItemCount > previousTotalItemCount) {
//            loading = false
//            previousTotalItemCount = totalItemCount
//        }
//
//        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
//            currentPage++
//            onLoadMore(currentPage, totalItemCount, view)
//            loading = true
//        }
//    }
//
//    fun resetState() {
//        this.currentPage = this.startingPageIndex
//        this.previousTotalItemCount = 0
//        this.loading = true
//    }
//
//    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)
//
//}
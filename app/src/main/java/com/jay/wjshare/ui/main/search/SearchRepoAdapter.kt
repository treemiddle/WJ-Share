package com.jay.wjshare.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jay.wjshare.databinding.ItemRepoBinding
import com.jay.wjshare.ui.base.BaseListAdapter
import com.jay.wjshare.ui.base.BaseViewHolder
import com.jay.wjshare.ui.main.search.model.RepoModel

class SearchRepoAdapter : BaseListAdapter<RepoModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RepoModel> {
        return SearchHolder.create(parent)
    }

    class SearchHolder(
        private val binding: ItemRepoBinding
    ) : BaseViewHolder<RepoModel>(binding) {

        override fun bind(item: RepoModel) {
            binding.item = item
            binding.executePendingBindings()
        }

        override fun recycle() {
            binding.tvTitle.text = null
            binding.tvDesc.text = null
            binding.tvStarCount.text = null
        }

        companion object {
            fun create(parent: ViewGroup): SearchHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ItemRepoBinding.inflate(layoutInflater, parent, false)

                return SearchHolder(view)
            }
        }
    }
}
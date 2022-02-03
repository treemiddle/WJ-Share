package com.jay.wjshare.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jay.wjshare.databinding.ItemRepoBinding
import com.jay.wjshare.ui.base.BaseListAdapter
import com.jay.wjshare.ui.base.BaseViewHolder
import com.jay.wjshare.ui.base.WJClickable
import com.jay.wjshare.ui.model.RepoModel

class SearchRepoAdapter : BaseListAdapter<RepoModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RepoModel> {
        return SearchHolder.create(parent).also {
            it.itemView.setOnClickListener { _ ->
                val currentItem = currentList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                (currentItem as? WJClickable<RepoModel>)?.onClick?.onNext(currentItem)
            }
        }
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
            Glide.with(binding.ivStar).clear(binding.ivStar)
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
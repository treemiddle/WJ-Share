package com.jay.wjshare.utils.bindingadapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.jay.wjshare.R

@SuppressLint("SetTextI18n")
@BindingAdapter("setOnwerName", "setRepoName")
fun bindTitle(tv: TextView, onwer: String, repo: String) {
    tv.text = "$onwer/$repo"
}

@BindingAdapter("setRepoDescription")
fun bindDescription(tv: TextView, description: String?) {
    with(tv) {
        if (description.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            text = description
        }
    }
}

@BindingAdapter("setHasLiked")
fun bindHasLiked(iv: ImageView, hasLiked: Boolean) {
    iv.background = if (hasLiked) {
        ContextCompat.getDrawable(iv.context, R.drawable.ic_star_selected)
    } else {
        ContextCompat.getDrawable(iv.context, R.drawable.ic_star_non_selected)
    }
}

@BindingAdapter("setStarCount")
fun bindStarCount(tv: TextView, count: Int) {
    tv.text = count.toString()
}
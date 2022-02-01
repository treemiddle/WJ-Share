package com.jay.wjshare.utils.bindingadapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

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

@BindingAdapter("setStarCount")
fun bindStarCount(tv: TextView, count: Int) {
    tv.text = count.toString()
}
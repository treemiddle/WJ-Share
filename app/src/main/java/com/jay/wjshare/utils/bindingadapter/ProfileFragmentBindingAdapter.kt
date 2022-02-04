package com.jay.wjshare.utils.bindingadapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setMyInfoProfile")
fun bindMyInfoProfile(iv: ImageView, path: String?) {
    Glide.with(iv)
        .load(path)
        .circleCrop()
        .into(iv)
}

@BindingAdapter("setMyInfoUserName")
fun bindMyInfoUserName(tv: TextView, name: String?) {
    name?.let { tv.text = it }
}
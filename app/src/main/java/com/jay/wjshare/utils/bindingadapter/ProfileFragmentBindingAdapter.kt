package com.jay.wjshare.utils.bindingadapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jay.wjshare.R

@BindingAdapter("setMyInfoProfile")
fun bindMyInfoProfile(iv: ImageView, path: String?) {
    Glide.with(iv)
        .load(path)
        .circleCrop()
//        .apply(
//            RequestOptions()
//                .placeholder(R.drawable.ic_home)
//                .error(R.drawable.ic_error))
        .into(iv)
}

@BindingAdapter("setMyInfoUserName")
fun bindMyInfoUserName(tv: TextView, name: String?) {
    if (name.isNullOrEmpty()) {
        tv.text = tv.context.getString(R.string.empty_user_name)
    } else {
        tv.text = name
    }
}
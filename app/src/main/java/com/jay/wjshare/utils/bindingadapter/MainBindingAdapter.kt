package com.jay.wjshare.utils.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jay.wjshare.R
import com.jay.wjshare.ui.main.MainViewModel
import com.jay.wjshare.utils.enums.ScreenType

@BindingAdapter("setLoginTitleState")
fun bindLoginTitleState(tv: TextView, state: Boolean) {
    tv.text = if (state) {
        tv.context.getString(R.string.fragment_logout_title)
    } else {
        tv.context.getString(R.string.fragment_login_title)
    }
}

@BindingAdapter("setLoginState")
fun bindLoginState(tv: TextView, state: Boolean) {
    tv.text = if (state) {
        tv.context.getString(R.string.logout_txt)
    } else {
        tv.context.getString(R.string.login_txt)
    }
}

@BindingAdapter("setBottomNavigation")
fun bindBottomNavigation(bottomNavi: BottomNavigationView, vm: MainViewModel?) {
    with(bottomNavi) {
        selectedItemId = R.id.search

        setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> vm?.onBottomNavigationListener(ScreenType.SEARCH)
                R.id.profile -> vm?.onBottomNavigationListener(ScreenType.PROFILE)
                else -> vm?.onBottomNavigationListener(ScreenType.SEARCH)
            }
            true
        }
    }
}
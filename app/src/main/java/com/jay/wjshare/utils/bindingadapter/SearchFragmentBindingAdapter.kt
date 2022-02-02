package com.jay.wjshare.utils.bindingadapter

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.jay.wjshare.ui.main.search.SearchViewModel

@BindingAdapter("setQueryListener")
fun bindQueryListener(et: EditText, vm: SearchViewModel?) {
    et.addTextChangedListener { vm?.debounceQuery(it.toString()) }
}
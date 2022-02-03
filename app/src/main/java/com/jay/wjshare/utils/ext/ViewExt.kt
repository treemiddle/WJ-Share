package com.jay.wjshare.utils.ext

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.GravityInt
import com.google.android.material.snackbar.Snackbar
import com.jay.wjshare.R

fun View.showSnackbar(message: String, @GravityInt gravity: Int) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        setAction(context.getString(R.string.check)) { dismiss() }
    }

    val view = snackbar.view
    val parms = view.layoutParams as FrameLayout.LayoutParams
    parms.gravity = gravity
    view.layoutParams = parms

    snackbar.show()
}
package com.jay.wjshare.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jay.wjshare.R
import com.jay.wjshare.ui.navigator.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator.initNavigator()
    }

}
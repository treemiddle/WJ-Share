package com.jay.wjshare.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.jay.wjshare.R
import com.jay.wjshare.databinding.ActivityMainBinding
import com.jay.wjshare.ui.navigator.MainNavigator
import com.jay.wjshare.utils.ScreenType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    @Inject
    lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.run {
            lifecycleOwner = this@MainActivity
            vm = viewModel
        }

        navigator.initNavigator()

        setupObserving()
    }

    private fun setupObserving() {
        with(viewModel) {
            bottomNaviType.observe(this@MainActivity, {
                navigator.screenTo(it)
            })
        }
    }

}
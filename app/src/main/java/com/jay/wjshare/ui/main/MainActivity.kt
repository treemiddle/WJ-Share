package com.jay.wjshare.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jay.wjshare.R
import com.jay.wjshare.databinding.ActivityMainBinding
import com.jay.wjshare.ui.navigator.MainNavigator
import com.jay.wjshare.utils.EventObserver
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
                when (it) {
                    ScreenType.PROFILE -> {
                        if (getAccessToken().isEmpty()) {
                            navigator.screenTo(ScreenType.LOGIN)
                        } else {
                            navigator.screenTo(it)
                        }
                    }
                    else -> navigator.screenTo(it)
                }
            })
            loginUrl.observe(this@MainActivity, EventObserver {
                goGitHub(it)
            })
        }
    }

    private fun goGitHub(loginUri: Uri?) {
        loginUri?.let {
            Intent(Intent.ACTION_VIEW).apply {
                data = it
                startActivity(this)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.getQueryParameter(CODE)?.let { code ->
            viewModel.requestAccessToken(code)
        }
    }

    companion object {
        private const val CODE = "code"
    }
}
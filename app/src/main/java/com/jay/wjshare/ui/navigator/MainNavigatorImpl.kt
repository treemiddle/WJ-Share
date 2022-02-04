package com.jay.wjshare.ui.navigator

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.jay.wjshare.R
import com.jay.wjshare.ui.main.login.LoginFragment
import com.jay.wjshare.ui.main.profile.ProfileFragment
import com.jay.wjshare.ui.main.search.SearchFragment
import com.jay.wjshare.utils.TAG_LOGIN
import com.jay.wjshare.utils.TAG_PROFILE
import com.jay.wjshare.utils.TAG_SEARCH
import com.jay.wjshare.utils.enums.ScreenType
import javax.inject.Inject

class MainNavigatorImpl @Inject constructor(
    private val fragmentActivity: FragmentActivity
) : MainNavigator {

    private val searchFragment by lazy { SearchFragment.newInstance() }
    private val profileFragment by lazy { ProfileFragment.newInstance() }
    private val loginFragment by lazy { LoginFragment.newInstance() }

    override fun initNavigator() = initFragment()

    override fun screenTo(screen: ScreenType) = replaceFragment(screen)

    private fun initFragment() {
        fragmentActivity.supportFragmentManager.commit {
            add(R.id.fl_container, searchFragment, TAG_SEARCH)
            show(searchFragment)
        }
    }

    private fun replaceFragment(screen: ScreenType) {
        fragmentActivity.supportFragmentManager.commit {
            when (screen) {
                ScreenType.SEARCH -> {
                    try {
                        hide(profileFragment)
                        hide(loginFragment)
                        show(searchFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                ScreenType.PROFILE -> {
                    val profile = fragmentActivity.supportFragmentManager.findFragmentByTag(TAG_PROFILE)

                    if (profile == null) {
                        add(R.id.fl_container, profileFragment, TAG_PROFILE)
                    }

                    try {
                        hide(searchFragment)
                        hide(loginFragment)
                        show(profileFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                ScreenType.LOGIN -> {
                    val login = fragmentActivity.supportFragmentManager.findFragmentByTag(TAG_LOGIN)

                    if (login == null) {
                        add(R.id.fl_container, loginFragment, TAG_LOGIN)
                    }

                    try {
                        hide(profileFragment)
                        hide(searchFragment)
                        show(loginFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}
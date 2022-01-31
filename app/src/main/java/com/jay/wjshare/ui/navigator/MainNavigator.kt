package com.jay.wjshare.ui.navigator

import com.jay.wjshare.utils.ScreenType

interface MainNavigator {

    fun initNavigator()

    fun screenTo(screen: ScreenType)

}
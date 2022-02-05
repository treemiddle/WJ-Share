package com.jay.wjshare.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jay.wjshare.domain.repository.AuthRepository
import com.jay.wjshare.livedata.getOrAwaitValue
import com.jay.wjshare.ui.main.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest  {

    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule()

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    lateinit var authRepository: AuthRepository

    // mock객체가 제대로 생성되지 않는 거 같은데
    // mainviewmodel 초기화 블럭이 없으면 잘 진행됨...아직 이유 못찾음
    @Before
    fun setup() {
        viewModel = MainViewModel(authRepository)
    }

    @Test
    fun `뷰모델이 생성되면 getAccessToken은 null을 반환한다`() {
        val token = viewModel.getAccessToken()

        Assert.assertEquals(null, token)
    }

    @Test
    fun `getAccessToken()이 비어있지 않으면 기대값은 false다`() {
        val test = viewModel.loginState.getOrAwaitValue()

        Assert.assertEquals(false, test)
    }

    @Test
    fun `a`() {

    }


}
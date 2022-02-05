package com.jay.wjshare.repository

import com.jay.wjshare.data.AuthRepositoryImpl
import com.jay.wjshare.data.local.source.AuthLocalDataSource
import com.jay.wjshare.data.remote.source.AuthRemoteDataSource
import com.jay.wjshare.domain.repository.AuthRepository
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    var rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var local: AuthLocalDataSource

    @Mock
    lateinit var remote: AuthRemoteDataSource

    private lateinit var impl: AuthRepository

    @Before
    fun setup() {
        impl = AuthRepositoryImpl(local, remote)
    }

    @Test
    fun `로컬에 토큰이 없으면 accessToken은 null을 반환한다`() {
        val token = impl.accessToken

        Assert.assertEquals(null, token)
    }

    @Test
    fun `accessToken을 확인해 값이 있으면 null이 아니다`() {
        `when`(local.accessToken).thenReturn("imtoken")

        val token = impl.accessToken

        Assert.assertNotNull(token)
    }

    @Test
    fun `requestAccessToken을 요청해서 응답(토큰)값과 로컬에 저장된 값이 일치한다`() {
        `when`(remote.requestAccessToken("wj")).thenReturn(Single.just("token"))
        `when`(local.accessToken).thenReturn("token")

        impl.requestAccessToken("wj")
            .test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertOf {
                it.assertSubscribed()
                it.assertComplete()
                Assert.assertEquals(impl.accessToken, it.values()[0])
            }
    }

    @Test
    fun `로컬에 저장된 값을 지우면 null을 반환한다`() {
        impl.clear()

        Assert.assertEquals(null, impl.accessToken)
    }

}
package com.sample.currencyconversion.core.domain

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import com.sample.currencyconversion.freature.converter.domain.usecase.CacheCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.GetRemoteCurrencyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.fail


@RunWith(JUnit4::class)
class GetRemoteCurrencyUseCaseTest {
    private lateinit var useCase: GetRemoteCurrencyUseCase

    private val remoteRepository: RemoteRepository =  Mockito.mock()
    private val cacheCurrencyUseCase: CacheCurrencyUseCase =  Mockito.mock()

    private val fakeExchangeRate: ExchangeRate = Mockito.mock()
    private val testAppId = "test_app_id"

    @Before
    fun setUp() {
        useCase = GetRemoteCurrencyUseCase(remoteRepository, cacheCurrencyUseCase)
    }

    @Test
    fun invoke_whenApiCallSuccess_shouldCacheData() = runTest {
        val expected = AppResult.Success(fakeExchangeRate)
        whenever(remoteRepository.getExchangeRatesList(testAppId)).thenReturn(expected)

        val result = useCase(testAppId)

        assertEquals(expected, result)
        verify(cacheCurrencyUseCase).invoke(fakeExchangeRate)
    }

    @Test
    fun invoke_whenApiReturnsError_shouldNotCallCacheUseCase() = runTest {
        val expected = AppResult.Error(500, "API error")
        whenever(remoteRepository.getExchangeRatesList(testAppId)).thenReturn(expected)

        val result = useCase(testAppId)

        assertEquals(expected, result)
        verify(cacheCurrencyUseCase, never()).invoke(any())
    }

    @Test
    fun invoke_whenRepositoryThrows_shouldThrowException() = runTest {
        val exception = RuntimeException("Network crash")
        whenever(remoteRepository.getExchangeRatesList(testAppId)).thenThrow(exception)

        try {
            useCase(testAppId)
            fail("Exception was expected")
        } catch (e: RuntimeException) {
            assertEquals("Network crash", e.message)
        }
    }
}

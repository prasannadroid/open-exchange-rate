package com.sample.currencyconversion.core.domain

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.usecase.GetCachedCurrencyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class GetCachedCurrencyUseCaseTest {

    private lateinit var getCachedCurrencyUseCaseTest: GetCachedCurrencyUseCase

    private val localRepository: LocalRepository = Mockito.mock()

    private val exchangeRate: ExchangeRate = Mockito.mock()

    private val successResult = AppResult.Success(exchangeRate)

    private val errorResult = AppResult.Error(500, "Unknown Error")

    private val exceptionResult = AppResult.Exception(Throwable())

    @Before
    fun setup() = runTest {
        getCachedCurrencyUseCaseTest = GetCachedCurrencyUseCase(localRepository)
    }

    @Test
    fun invoke_whenSavedExchangeRateIsAvailable_returnsSuccess() = runTest {
        whenever(localRepository.getSavedExchangeRate()).thenReturn(successResult)

        val result = getCachedCurrencyUseCaseTest()
        assertEquals(result, successResult)
    }

    @Test
    fun invoke_whenSavedExchangeRateReturnsError_returnsError() = runTest {
        whenever(localRepository.getSavedExchangeRate()).thenReturn(errorResult)

        val result = getCachedCurrencyUseCaseTest()
        assertEquals(result, errorResult)
    }

    @Test
    fun invoke_whenSavedExchangeRateThrowsException_returnsException() = runTest {
        whenever(localRepository.getSavedExchangeRate()).thenReturn(exceptionResult)

        val result = getCachedCurrencyUseCaseTest()
        assertEquals(result, exceptionResult)
    }
}

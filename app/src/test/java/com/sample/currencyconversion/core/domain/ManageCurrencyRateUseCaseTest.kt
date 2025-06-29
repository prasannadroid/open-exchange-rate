package com.sample.currencyconversion.core.domain

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.usecase.GetCachedCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.GetRemoteCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.ManageCurrencyRateUseCase
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(JUnit4::class)
class ManageCurrencyRateUseCaseTest {

    private lateinit var exchangeRateUserCase: ManageCurrencyRateUseCase

    private val localRepository: LocalRepository = Mockito.mock()

    private val getRemoteCurrencyUseCase: GetRemoteCurrencyUseCase = Mockito.mock()

    private val getCachedCurrencyUseCase: GetCachedCurrencyUseCase = Mockito.mock()

    private val exchangeRate: ExchangeRate = Mockito.mock()

    private val expectedSuccessResponse = AppResult.Success(exchangeRate)

    private val expectedErrorResponse = AppResult.Error(500, "Unknown Error")

    @Before
    fun setup() = runTest {
        exchangeRateUserCase = ManageCurrencyRateUseCase(
            localRepository,
            getRemoteCurrencyUseCase,
            getCachedCurrencyUseCase
        )
    }

    @Test
    fun invoke_whenDataIsCached_shouldReturnCachedResponse() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(getCachedCurrencyUseCase.invoke()).thenReturn(
            AppResult.Success(exchangeRate)
        )

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        verify(getCachedCurrencyUseCase).invoke()
        assertEquals(expectedSuccessResponse, result)
    }

    @Test
    fun invoke_whenAppIdIsInvalid_shouldNotReturnCachedResponse() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(false)
        whenever(getRemoteCurrencyUseCase.invoke(TestHelper.INVALID_APP_ID)).thenReturn(
            AppResult.Error(401, "Unauthorized")
        )

        val result = exchangeRateUserCase(TestHelper.INVALID_APP_ID)

        assertEquals(AppResult.Error(401, "Unauthorized"), result)
    }

    @Test
    fun invoke_whenDataIsNotCached_shouldReturnApiResponse() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(false)
        whenever(getRemoteCurrencyUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(exchangeRate)
        )

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        verify(getRemoteCurrencyUseCase).invoke(TestHelper.VALID_APP_ID)
        assertEquals(AppResult.Success(exchangeRate), result)
    }

    @Test
    fun invoke_whenCachedDataAvailable_shouldCallGetCachedCurrency() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)

        exchangeRateUserCase(TestHelper.VALID_APP_ID)

        verify(getCachedCurrencyUseCase).invoke()
    }

    @Test
    fun invoke_whenRemoteUseCaseReturnsError_shouldReturnError() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(false)
        whenever(getRemoteCurrencyUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Error(500, "Remote Error")
        )

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(AppResult.Error(500, "Remote Error"), result)
    }

    @Test
    fun invoke_whenCachedUseCaseReturnsError_shouldReturnError() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(getCachedCurrencyUseCase.invoke()).thenReturn(AppResult.Error(500, "Cache Error"))

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(AppResult.Error(500, "Cache Error"), result)
    }

    @Test
    fun invoke_whenIsCachedThrowsException_shouldReturnError() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenThrow(RuntimeException("DB Failure"))

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(expectedErrorResponse, result)
    }

    @Test
    fun invoke_whenRemoteUseCaseThrowsException_shouldReturnError() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(false)
        whenever(getRemoteCurrencyUseCase.invoke(TestHelper.VALID_APP_ID)).thenThrow(
            RuntimeException("API Crash")
        )

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(expectedErrorResponse, result)
    }

    @Test
    fun invoke_whenCachedUseCaseThrowsException_shouldReturnError() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(getCachedCurrencyUseCase.invoke()).thenThrow(RuntimeException("SP Crash"))

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(expectedErrorResponse, result)
    }

    @Test
    fun invoke_whenIsCachedReturnsNull_shouldReturnRemoteResponse() = runTest {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(null)
        whenever(getRemoteCurrencyUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(exchangeRate)
        )

        val result = exchangeRateUserCase(TestHelper.VALID_APP_ID)

        assertEquals(expectedSuccessResponse, result)
    }

    @Test
    fun invoke_whenCalledWithoutSetup_shouldReturnNull() = runTest {
        val result = exchangeRateUserCase.invoke(TestHelper.VALID_APP_ID)
        assertNull(result)
    }
}

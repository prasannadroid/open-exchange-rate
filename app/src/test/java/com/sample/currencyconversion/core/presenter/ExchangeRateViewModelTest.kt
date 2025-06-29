package com.sample.currencyconversion.core.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.core.data.api.apistate.ProgressState
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.usecase.GetConvertCacheListUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.ManageCurrencyRateUseCase
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.ExchangeRateViewModel
import com.sample.currencyconversion.helper.TestDispatcherProvider
import com.sample.currencyconversion.helper.TestHelper
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ExchangeRateViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ExchangeRateViewModel

    private val exchangeRatesResponse: ExchangeRate = Mockito.mock()

    private val fetchCurrencyRateUseCase: ManageCurrencyRateUseCase = Mockito.mock()

    private val getConvertCacheListUseCase: GetConvertCacheListUseCase = Mockito.mock()

    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    private val myKeyProvider: MyKeyProvider = Mockito.mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ExchangeRateViewModel(
            fetchCurrencyRateUseCase,
            myKeyProvider,
            dispatcherProvider,
            getConvertCacheListUseCase
        )
    }

    @After
    fun tearDown() = runTest {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun getExchangeRates_whenApiCallSuccessful_shouldUpdateUiStateAndRemoteExchangeRate() = runTest {
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.VALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(exchangeRatesResponse)
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiStateStateFlow.value
        assertEquals(ProgressState.STOP, uiState.isLoading)
        assertEquals("Successfully fetch the currency", uiState.message)
        assertEquals(exchangeRatesResponse, viewModel.remoteExchangeRate.value)
    }

    @Test
    fun getExchangeRates_whenApiCallReturnsNull_shouldShowUnknownError() = runTest {
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.VALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(null)
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiStateStateFlow.value
        assertEquals(ProgressState.STOP, uiState.isLoading)
        assertEquals("Unknown Error", uiState.message)
    }

    @Test
    fun getExchangeRates_whenApiCallReturnsError_shouldUpdateUiStateWithError() = runTest {
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.INVALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.INVALID_APP_ID)).thenReturn(
            AppResult.Error(401, "unauthorized")
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiStateStateFlow.value
        assertEquals(ProgressState.STOP, uiState.isLoading)
        assertEquals("unauthorized", uiState.message)
        assertEquals(401, uiState.errorCode)
    }

    @Test
    fun getExchangeRates_whenApiCallThrowsException_shouldUpdateUiStateWithExceptionMessage() = runTest {
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.VALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Exception(Throwable("Exception occurred"))
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiStateStateFlow.value
        assertEquals(ProgressState.STOP, uiState.isLoading)
        assertEquals("Exception occurred", uiState.message)
    }

    @Test
    fun getArrayListFromCurrencyMap_whenMapIsNotEmpty_shouldReturnCurrencyList() = runTest {
        val map = mapOf("USD" to 1.0, "AED" to 1.5)
        val list = listOf(Currency("USD", 1.0), Currency("AED", 1.5))

        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.VALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(
                ExchangeRate(
                    disclaimer = "",
                    license = "",
                    timestamp = 0L,
                    base = "USD",
                    ratesMap = map
                )
            )
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        whenever(getConvertCacheListUseCase.invoke(map)).thenReturn(list)

        viewModel.getArrayListFromCurrencyMap()
        testDispatcher.scheduler.advanceUntilIdle()

        val currencyList = viewModel.currencyListStateFlow.value
        assertEquals(2, currencyList?.size)
        assertFalse(currencyList.isNullOrEmpty())
        assertEquals(1.0, currencyList!![0].currencyValue)
        assertEquals(1.5, currencyList[1].currencyValue)
    }

    @Test
    fun getArrayListFromCurrencyMap_whenMapIsEmpty_shouldReturnEmptyList() = runTest {
        val map = emptyMap<String, Double>()
        val list = emptyList<Currency>()

        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn(TestHelper.VALID_APP_ID)
        whenever(fetchCurrencyRateUseCase.invoke(TestHelper.VALID_APP_ID)).thenReturn(
            AppResult.Success(
                ExchangeRate(
                    disclaimer = "",
                    license = "",
                    timestamp = 0L,
                    base = "USD",
                    ratesMap = map
                )
            )
        )

        viewModel.getExchangeRates()
        testDispatcher.scheduler.advanceUntilIdle()

        whenever(getConvertCacheListUseCase.invoke(map)).thenReturn(list)

        viewModel.getArrayListFromCurrencyMap()
        testDispatcher.scheduler.advanceUntilIdle()

        val currencyList = viewModel.currencyListStateFlow.value
        assertEquals(0, currencyList?.size)
        assertTrue(currencyList?.isEmpty() == true)
    }
}

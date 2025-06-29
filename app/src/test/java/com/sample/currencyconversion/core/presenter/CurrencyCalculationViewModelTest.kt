package com.sample.currencyconversion.core.presenter

import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.CurrencyCalculationViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
@RunWith(JUnit4::class)
class CurrencyCalculationViewModelTest {

    private lateinit var viewModel: CurrencyCalculationViewModel

    @Before
    fun setUp() {
        viewModel = CurrencyCalculationViewModel()
    }

    @Test
    fun updateBaseCurrencyState_whenCalled_shouldUpdateBaseCurrencyCode() = runTest {
        val newBaseCode = "LKR"
        val initialBaseCode = viewModel.baseCurrencyCode.value

        viewModel.updateBaseCurrencyState(newBaseCode)

        assertEquals("USD", initialBaseCode)
        assertEquals(newBaseCode, viewModel.baseCurrencyCode.value)
    }

    @Test
    fun updateBaseCurrencyValue_whenCalled_shouldUpdateBaseCurrencyValue() = runTest {
        val newBaseValue = 100.0
        val initialBaseValue = viewModel.baseCurrencyValue.value

        viewModel.updateBaseCurrencyValue(newBaseValue)

        assertEquals(1.0, initialBaseValue)
        assertEquals(newBaseValue, viewModel.baseCurrencyValue.value)
    }

    @Test
    fun updateInputCurrencyValue_whenCalled_shouldUpdateInputCurrencyValue() = runTest {
        val newInputValue = 100.0
        val initialInputValue = viewModel.inputCurrencyValue.value

        viewModel.updateInputCurrencyValue(newInputValue)

        assertEquals(1.0, initialInputValue)
        assertEquals(newInputValue, viewModel.inputCurrencyValue.value)
    }

    @Test
    fun calculateExchangeRate_whenCurrencyIsUSD_shouldReturnInputValue() = runTest {
        val baseValue = 1.0
        val input = 100.0
        val usd = Currency("USD", 1.0)

        val result = viewModel.calculateExchangeRate(usd, baseValue, input)

        assertEquals(100.0, result, 0.0)
    }

    @Test
    fun calculateExchangeRate_whenCurrencyIsLKR_shouldReturnConvertedValue() = runTest {
        val baseValue = 1.0
        val input = 100.0
        val lkr = Currency("LKR", 300.0)

        val result = viewModel.calculateExchangeRate(lkr, baseValue, input)

        assertEquals(30000.0, result, 0.0)
    }

    @Test
    fun calculateExchangeRate_whenCurrencyIsINR_shouldReturnConvertedValue() = runTest {
        val baseValue = 1.0
        val input = 100.0
        val inr = Currency("INR", 86.58)

        val result = viewModel.calculateExchangeRate(inr, baseValue, input)

        assertEquals(8658.0, result, 0.0)
    }
}

package com.sample.currencyconversion.freature.converter.presenter.viewmodel

import androidx.lifecycle.ViewModel
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrencyCalculationViewModel : ViewModel() {

    // manage initial and selected currency code
    private val _baseCurrencyCode = MutableStateFlow("USD")
    val baseCurrencyCode: StateFlow<String> = _baseCurrencyCode

    // initial value is USD initial value will be 1.0
    private val _baseCurrencyValue = MutableStateFlow(1.0)
    val baseCurrencyValue: StateFlow<Double> = _baseCurrencyValue

    // user input value / default value is 1.0
    private val _inputCurrencyValue = MutableStateFlow(1.0)
    val inputCurrencyValue: StateFlow<Double> = _inputCurrencyValue

    fun updateBaseCurrencyState(state: String) {
        _baseCurrencyCode.value = state
    }

    fun updateBaseCurrencyValue(currencyValue: Double) {
        _baseCurrencyValue.value = currencyValue
    }

    fun updateInputCurrencyValue(currencyValue: Double) {
        _inputCurrencyValue.value = currencyValue
    }

    fun calculateExchangeRate(currency: Currency, baseCurrency:Double, userInputCurrency:Double):Double {
        return (currency.currencyValue / baseCurrency) * userInputCurrency
    }
}
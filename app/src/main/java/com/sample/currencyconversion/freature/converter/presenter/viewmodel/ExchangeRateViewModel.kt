package com.sample.currencyconversion.freature.converter.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.common.ui.state.UiState
import com.sample.currencyconversion.core.coroutine.CoroutineDispatcherProvider
import com.sample.currencyconversion.core.data.api.apistate.ProgressState
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.usecase.GetConvertCacheListUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.ManageCurrencyRateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ExchangeRateViewModel(
    private val fetchCurrencyRateUseCase: ManageCurrencyRateUseCase,
    private val myKeyProvider: MyKeyProvider,
    private val appDispatcher: CoroutineDispatcherProvider,
    private val getConvertCacheListUseCase: GetConvertCacheListUseCase
) :
    ViewModel() {

    // manage ui state
    private val _uiState = MutableStateFlow(UiState())
    val uiStateStateFlow: StateFlow<UiState> = _uiState

    private val _currencyListState = MutableStateFlow<List<Currency>?>(null)
    val currencyListStateFlow: StateFlow<List<Currency>?> = _currencyListState

    // manage API call
    private val _latestExchangeRate = MutableStateFlow(
        ExchangeRate(
            disclaimer = "",
            license = "",
            timestamp = 0L,
            base = "",
            ratesMap = emptyMap()
        )
    )

    val remoteExchangeRate: StateFlow<ExchangeRate> = _latestExchangeRate

    init {
        _uiState.value = UiState(isLoading = ProgressState.IDLE)
    }

    fun getExchangeRates() {
        viewModelScope.launch(appDispatcher.io) {
            // set UiState to started
            _uiState.value = UiState(isLoading = ProgressState.STARTED)

            val result = fetchCurrencyRateUseCase(myKeyProvider.readFromPropertiesFile())

            val newState = when (result) {
                is AppResult.Success -> {
                    result.data?.let {
                        _latestExchangeRate.value = it
                        UiState(
                            isLoading = ProgressState.STOP,
                            message = "Successfully fetch the currency"
                        )
                    } ?: UiState(
                        isLoading = ProgressState.STOP,
                        message = "Unknown Error"
                    )

                }

                is AppResult.Error -> UiState(
                    isLoading = ProgressState.STOP,
                    message = result.message,
                    errorCode = result.code
                )

                is AppResult.Exception -> UiState(
                    isLoading = ProgressState.STOP,
                    message = result.throwable.message
                )
            }
            // update final state
            _uiState.value = newState
        }
    }


    fun getArrayListFromCurrencyMap() = viewModelScope.launch {
        _currencyListState.value = getConvertCacheListUseCase(remoteExchangeRate.value.ratesMap)
    }


}
package com.sample.currencyconversion.koin

import com.sample.currencyconversion.freature.converter.presenter.viewmodel.CurrencyCalculationViewModel
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.ExchangeRateViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        ExchangeRateViewModel(get(), get(), get(), get())
    }

    viewModel {
        CurrencyCalculationViewModel()
    }

}
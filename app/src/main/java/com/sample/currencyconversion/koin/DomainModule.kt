package com.sample.currencyconversion.koin

import com.sample.currencyconversion.freature.converter.domain.usecase.CacheCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.GetCachedCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.GetConvertCacheListUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.GetRemoteCurrencyUseCase
import com.sample.currencyconversion.freature.converter.domain.usecase.ManageCurrencyRateUseCase
import org.koin.dsl.module

// create all the domain modules including UseCases
val domainModule = module {
    single {
        ManageCurrencyRateUseCase(get(), get(), get())
    }

    single {
        CacheCurrencyUseCase(get())
    }

    single {
        GetCachedCurrencyUseCase(get())
    }

    single {
        GetRemoteCurrencyUseCase(get(), get())
    }

    single {
        GetConvertCacheListUseCase(get())
    }


}
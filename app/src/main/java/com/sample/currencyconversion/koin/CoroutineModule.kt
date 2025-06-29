package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.coroutine.CoroutineDispatcherProvider
import com.sample.currencyconversion.core.coroutine.DefaultDispatcherProvider
import org.koin.dsl.module

val coroutineModule = module {
    single<CoroutineDispatcherProvider> {
        DefaultDispatcherProvider()
    }
}
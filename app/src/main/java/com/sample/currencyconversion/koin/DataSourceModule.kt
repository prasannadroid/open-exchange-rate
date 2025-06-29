package com.sample.currencyconversion.koin

import com.sample.currencyconversion.freature.converter.data.source.local.LocalDataSource
import com.sample.currencyconversion.freature.converter.data.source.remote.RemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        RemoteDataSource(get())
    }
    single {
        LocalDataSource(get())
    }
}
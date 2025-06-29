package com.sample.currencyconversion.koin

import com.sample.currencyconversion.freature.converter.data.repository.LocalRepositoryImpl
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import com.sample.currencyconversion.freature.converter.data.repository.RemoteRepositoryImpl
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<RemoteRepository> {
        RemoteRepositoryImpl(get())
    }

    single<LocalRepository> {
        LocalRepositoryImpl(get())
    }
}
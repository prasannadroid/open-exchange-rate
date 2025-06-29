package com.sample.currencyconversion.koin

import com.sample.currencyconversion.core.data.scheduler.MyScheduler
import org.koin.dsl.module

val jobModule = module {

    factory { MyScheduler(get()) } // Inject context and workerParams
}
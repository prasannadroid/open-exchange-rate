package com.sample.currencyconversion.koin

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.sample.currencyconversion.core.data.local.MyDataStore
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.core.data.api.ApiService
import com.sample.currencyconversion.core.data.service.ExchangeRateJobRunner
import com.sample.currencyconversion.core.util.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        get<Application>().applicationContext
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
               level =  HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // use single scope to get a singleton retrofit object
    single {
        // making retrofit singleton object
        Retrofit.Builder().baseUrl(Const.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    single {
        PreferenceDataStoreFactory.create(produceFile = {
            androidContext().preferencesDataStoreFile(
                Const.DATA_STORE_PREF
            )
        })
    }

    single {
        MyDataStore(get())
    }

    single {
        MyKeyProvider(get())
    }

    single {
        ExchangeRateJobRunner(get(),get(),get())
    }

}

package com.sample.currencyconversion.core.data.service

import android.util.Log
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeRateJobRunner(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val myKeyProvider: MyKeyProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun runJob(): Unit = withContext(dispatcher) {
        val jobSavedState = localRepository.isExchangeRateJobExecuted()

        // prevent fetching the exchange rate from the first time
        if (jobSavedState == false || jobSavedState == null) {
            localRepository.saveExchangeRateJobState(true)
            return@withContext
        }

        // fetch exchange rates from the api
        val response = remoteRepository.getExchangeRateInBackground(
            myKeyProvider.readFromPropertiesFile(), this
        )

        when (response) {
            is ApiResponse.Success -> {
                response.data?.let {
                    localRepository.saveExchangeRateResponse(response.data)
                }
            }

            is ApiResponse.Error -> {
                //Log.d("ExchangeRateJobRunner", "Error Fetching Data")
            }

            is ApiResponse.Exception -> {
                //Log.d("ExchangeRateJobRunner", "Exception Occurred")
            }
        }

    }
}
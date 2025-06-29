package com.sample.currencyconversion.freature.converter.data.repository

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.freature.converter.data.source.remote.RemoteDataSource
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope

class RemoteRepositoryImpl(
    private val exchangeRatesDataSource: RemoteDataSource,
) : RemoteRepository {

    // Repository implementation method that fetches exchange rates
    // from the remote data source and maps the response to AppResult
    override suspend fun getExchangeRatesList(appId: String): AppResult<ExchangeRate> {

        // Call the remote data source (Retrofit/API) and get an ApiResponse
        return when (val apiResponse = exchangeRatesDataSource.fetchExchangeRates(appId)) {
            // If the API call was successful, wrap the data in AppResult.Success
            is ApiResponse.Success -> AppResult.Success(apiResponse.data)
            // If the API returned an error response (e.g., 404, 500), map it to AppResult.Error
            is ApiResponse.Error -> AppResult.Error(apiResponse.code, apiResponse.message)
            // If the API call threw an exception (e.g., IOException), map it to AppResult.Exception
            is ApiResponse.Exception -> AppResult.Exception(apiResponse.throwable)
        }
    }

    // get exchange rates in back ground by job scheduler
    override suspend fun getExchangeRateInBackground(
        appId: String, scope: CoroutineScope
    ) = exchangeRatesDataSource.fetchExchangeRates(appId)

}
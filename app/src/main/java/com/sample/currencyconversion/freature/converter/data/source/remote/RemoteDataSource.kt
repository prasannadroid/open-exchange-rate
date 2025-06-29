package com.sample.currencyconversion.freature.converter.data.source.remote

import com.sample.currencyconversion.core.data.api.ApiService
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.core.exception.handleException
import com.sample.currencyconversion.freature.converter.data.mapper.toDomain
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate

class RemoteDataSource(private val retrofitService: ApiService) {

    // fetch from the server
    suspend fun fetchExchangeRates(appId: String): ApiResponse<ExchangeRate> {
        return try {
            val response = retrofitService.fetchExchangeRates(appId)
            if (response.isSuccessful) {
                // convert dto to domain model
                ApiResponse.Success(response.body()?.toDomain())
            } else {
                // Handle error response
                ApiResponse.Error(response.code(), response.message())
            }
        } catch (e: Exception) {
            // handle exception
            handleException(e)
        }
    }

}

package com.sample.currencyconversion.freature.converter.domain.repository

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import kotlinx.coroutines.CoroutineScope

interface RemoteRepository {

    suspend fun getExchangeRatesList(appId: String): AppResult<ExchangeRate>

    suspend fun getExchangeRateInBackground(
        appId: String,
        scope: CoroutineScope
    ): ApiResponse<ExchangeRate>

}
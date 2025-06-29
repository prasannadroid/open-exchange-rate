package com.sample.currencyconversion.freature.converter.domain.usecase

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository

class GetRemoteCurrencyUseCase(
    private val remoteRepository: RemoteRepository,
    private val cacheCurrencyUseCase: CacheCurrencyUseCase
) {

    // get currency rate from the API
    suspend operator fun invoke(appId: String): AppResult<ExchangeRate> {
        // get API result
        val response = remoteRepository.getExchangeRatesList(appId)
        // check the api call is success
        if (response is AppResult.Success) {
            cacheCurrencyUseCase(response.data)
        }
        return response
    }
}
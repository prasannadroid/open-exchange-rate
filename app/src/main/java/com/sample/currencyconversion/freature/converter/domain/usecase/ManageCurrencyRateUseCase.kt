package com.sample.currencyconversion.freature.converter.domain.usecase

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository

open class ManageCurrencyRateUseCase(
    private val localRepository: LocalRepository,
    private val getRemoteCurrencyUseCase: GetRemoteCurrencyUseCase,
    private val getCachedCurrencyUseCase: GetCachedCurrencyUseCase
) {

    // return ApiResponse either remote or local based on the cached state
    suspend operator fun invoke(appId: String): AppResult<ExchangeRate?> {
        return try {
            // check persistence state
            val isCached = isCached() == true
            if (isCached) {
                // get cached response
                getCachedCurrencyUseCase.invoke()
            } else {
                // get API result
                getRemoteCurrencyUseCase.invoke(appId)
            }
        } catch (e: Exception) {
            AppResult.Error(500, "Unknown Error")
        }
    }

    private suspend fun isCached() = localRepository.isExchangeRateJobExecuted()

}
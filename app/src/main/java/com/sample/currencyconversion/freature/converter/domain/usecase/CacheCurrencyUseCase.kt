package com.sample.currencyconversion.freature.converter.domain.usecase

import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository

class CacheCurrencyUseCase(private val localRepository: LocalRepository) {

    // cache/save currency rate to SP
    suspend operator fun invoke(exchangeRate: ExchangeRate?): Boolean {
        // return if null
        if (exchangeRate == null) return false

        // save the exchange rate response to SP
        val isExchangeRateSaved = localRepository.saveExchangeRateResponse(exchangeRate)
        // save exchange rate state
        val isJobStateSaved = localRepository.saveExchangeRateJobState(true)

        return isJobStateSaved && isExchangeRateSaved
    }
}
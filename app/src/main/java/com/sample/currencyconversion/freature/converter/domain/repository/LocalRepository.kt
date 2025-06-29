package com.sample.currencyconversion.freature.converter.domain.repository

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate

interface LocalRepository {

    suspend fun saveExchangeRateResponse(exchangeRatesResponse: ExchangeRate?) :Boolean

    suspend fun getSavedExchangeRate(): AppResult<ExchangeRate?>

    suspend fun saveExchangeRateJobState(jobState: Boolean) :Boolean

    suspend fun isExchangeRateJobExecuted(): Boolean?

   fun getCurrencyListFromMap(map: Map<String, Double>?): List<Currency>?
}
package com.sample.currencyconversion.freature.converter.data.repository

import com.google.gson.Gson
import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.data.mapper.CurrencyMapper
import com.sample.currencyconversion.freature.converter.data.source.local.LocalDataSource
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class LocalRepositoryImpl(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    // get saved exchange rate json from local data storage
    override suspend fun getSavedExchangeRate(): AppResult<ExchangeRate?> {
        return try {
            val json = localDataSource.getSavedExchangeRateJson().firstOrNull()
            json?.let {
                val exchangeRate = CurrencyMapper.toExchangeRate(json)
                AppResult.Success(exchangeRate)
            } ?: AppResult.Error(404, "No cached exchange rate found")

        } catch (e: Throwable) {
            AppResult.Exception(e)
        }

    }

    // save exchange rate saved state to the local storage
    override suspend fun saveExchangeRateJobState(jobState: Boolean) =
        localDataSource.saveExchangeRateJobState(jobState)

    // check exchange rate has been saved to the local storage
    override suspend fun isExchangeRateJobExecuted() =
        localDataSource.isExchangeRateJobExecuted().first()

    // save exchange rate to data store as json
    override suspend fun saveExchangeRateResponse(exchangeRate: ExchangeRate?) =
        localDataSource.saveExchangeRate(Gson().toJson(exchangeRate))

    override fun getCurrencyListFromMap(map: Map<String, Double>?) =
        CurrencyMapper.toList(map)

}
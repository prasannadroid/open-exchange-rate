package com.sample.currencyconversion.freature.converter.domain.usecase

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository

class GetConvertCacheListUseCase(private val localRepository: LocalRepository) {

    suspend operator fun invoke(map: Map<String, Double>): List<Currency>? {
        if (map.isNotEmpty()) {
            return localRepository.getCurrencyListFromMap(map)
        }
        // get saved exchange rate if API call not provided the exchange rate map
        val savedResult = localRepository.getSavedExchangeRate()
        // fetch map from saved data
        val ratesMap = (savedResult as? AppResult.Success)?.data?.ratesMap

        // convert saved map to a list for rendering
        return ratesMap?.takeIf { it.isNotEmpty() }?.let {
            localRepository.getCurrencyListFromMap(it)
        }

    }

}
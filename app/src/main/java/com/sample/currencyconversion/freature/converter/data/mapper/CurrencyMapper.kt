package com.sample.currencyconversion.freature.converter.data.mapper

import com.google.gson.Gson
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate

object CurrencyMapper {
    /**
     * Converts a Map<String, Currency> into a List<Currency>
     * @param currencyMap Map where key is currency code and value is Currency object
     * @return List of Currency objects
     */
    fun toList(currencyMap: Map<String, Double>?): List<Currency>? {
        return currencyMap?.map { (code, rate) -> Currency(code, rate) }
    }

    fun toExchangeRate(cache: String?) = cache?.let {
        Gson().fromJson(cache, ExchangeRate::class.java)
    }
}
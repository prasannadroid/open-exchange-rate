package com.sample.currencyconversion.freature.converter.domain.model

data class ExchangeRate(
    val disclaimer: String = "",
    val license: String = "",
    val timestamp: Long = 0L,
    val base: String = "",
    val ratesMap: Map<String, Double> = emptyMap(),
)
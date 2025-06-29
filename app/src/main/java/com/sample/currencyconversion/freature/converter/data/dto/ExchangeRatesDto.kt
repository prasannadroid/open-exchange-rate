package com.sample.currencyconversion.freature.converter.data.dto

import com.google.gson.annotations.SerializedName


data class ExchangeRatesDto(
    @SerializedName("disclaimer") val disclaimer: String,
    @SerializedName("license") val license: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("base") val base: String,
    @SerializedName("rates") val ratesList: Map<String, Double>,
)

package com.sample.currencyconversion.core.data.api

import com.sample.currencyconversion.freature.converter.data.dto.ExchangeRatesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/latest.json") // app id is not using now 87927be417c248b496a1c0ceecd47f57
    suspend fun fetchExchangeRates(@Query("app_id") appId: String): Response<ExchangeRatesDto>

}

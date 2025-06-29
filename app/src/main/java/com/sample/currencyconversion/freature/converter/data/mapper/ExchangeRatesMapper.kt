package com.sample.currencyconversion.freature.converter.data.mapper

import com.sample.currencyconversion.freature.converter.data.dto.ExchangeRatesDto
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate

fun ExchangeRatesDto.toDomain(): ExchangeRate {
    return ExchangeRate(
        disclaimer = disclaimer,
        license = license,
        timestamp = timestamp,
        base = base,
        ratesMap = ratesList
    )
}

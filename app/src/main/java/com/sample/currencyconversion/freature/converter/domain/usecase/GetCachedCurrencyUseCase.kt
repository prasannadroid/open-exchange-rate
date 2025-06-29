package com.sample.currencyconversion.freature.converter.domain.usecase

import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository

class GetCachedCurrencyUseCase(private val localRepository: LocalRepository) {

    // get currency rate from SP/Local
    suspend operator fun invoke() = localRepository.getSavedExchangeRate()
}
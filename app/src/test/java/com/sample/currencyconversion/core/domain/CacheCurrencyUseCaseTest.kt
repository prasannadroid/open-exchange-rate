package com.sample.currencyconversion.core.domain

import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.usecase.CacheCurrencyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue


@RunWith(JUnit4::class)
class CacheCurrencyUseCaseTest {

    private lateinit var cacheCurrencyUseCase: CacheCurrencyUseCase

    private val localRepository: LocalRepository = Mockito.mock()

    private val exchangeRate: ExchangeRate = Mockito.mock()

    @Before
    fun setup() = runTest {
        cacheCurrencyUseCase = CacheCurrencyUseCase(localRepository)

    }

    // test with null value and false should return
    @Test
    fun invoke_saveExchangeRate_WithNullExchangeRate() = runTest {

        val isSaved = cacheCurrencyUseCase(null)
        assertFalse(isSaved)
    }

    // test both Exchange rate and job state save
    @Test
    fun invoke_saveExchangeRate_AndJobStateBoth_saved() = runTest {
        whenever(localRepository.saveExchangeRateResponse(exchangeRate)).thenReturn(true)
        whenever(localRepository.saveExchangeRateJobState(true)).thenReturn(true)

        val isSaved = cacheCurrencyUseCase(exchangeRate)
        assertTrue(isSaved)
    }

    // test Exchange rate saved and job state NOT saved
    @Test
    fun invoke_saveExchangeRateSaved_AndJobState_NotSaved() = runTest {
        whenever(localRepository.saveExchangeRateResponse(exchangeRate)).thenReturn(true)
        whenever(localRepository.saveExchangeRateJobState(true)).thenReturn(false)

        val isSaved = cacheCurrencyUseCase(exchangeRate)
        assertFalse(isSaved)
    }

    // test Exchange rate Not saved and job state saved
    @Test
    fun invoke_saveExchangeRateNotSaved_AndJobStateSaved() = runTest {
        whenever(localRepository.saveExchangeRateResponse(exchangeRate)).thenReturn(false)
        whenever(localRepository.saveExchangeRateJobState(true)).thenReturn(true)

        val isSaved = cacheCurrencyUseCase(exchangeRate)
        assertFalse(isSaved)
    }
}
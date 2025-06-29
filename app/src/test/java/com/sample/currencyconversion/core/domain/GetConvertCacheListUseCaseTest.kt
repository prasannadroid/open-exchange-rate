package com.sample.currencyconversion.core.domain

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.usecase.GetConvertCacheListUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(JUnit4::class)
class GetConvertCacheListUseCaseTest {

    private lateinit var useCase: GetConvertCacheListUseCase

    private val localRepository: LocalRepository = mock()

    private val givenMap = mapOf("USD" to 1.0, "AED" to 3.0, "LKR" to 300.0)

    private val expectedList = listOf(
        Currency("USD", 1.0),
        Currency("AED", 3.0),
        Currency("LKR", 300.0)
    )

    private val emptyMap = emptyMap<String, Double>()

    @Before
    fun setUp() {
        useCase = GetConvertCacheListUseCase(localRepository)
    }

    @Test
    fun invoke_whenGivenMapIsNotEmpty_returnsMappedCurrencyList() = runTest {
        whenever(localRepository.getCurrencyListFromMap(givenMap)).thenReturn(expectedList)

        val list = useCase.invoke(givenMap)

        assertNotNull(list)
        assertEquals(list.size, givenMap.size)
        assertEquals(expectedList, list)
        assertEquals("USD", list[0].currencyCode)
    }

    @Test
    fun invoke_whenGivenMapIsEmptyAndCacheAvailable_returnsMappedListFromCache() = runTest {
        val savedExchangeRate = mock<ExchangeRate>().apply {
            whenever(ratesMap).thenReturn(givenMap)
        }
        whenever(localRepository.getSavedExchangeRate()).thenReturn(AppResult.Success(savedExchangeRate))
        whenever(localRepository.getCurrencyListFromMap(givenMap)).thenReturn(expectedList)

        val list = useCase.invoke(emptyMap)

        assertNotNull(list)
        assertEquals(list.size, givenMap.size)
        assertEquals(expectedList, list)
        assertEquals("USD", list[0].currencyCode)
    }

    @Test
    fun invoke_whenRatesMapIsNullOrEmpty_returnsNull() = runTest {
        val savedExchangeRateWithNull = mock<ExchangeRate>().apply {
            whenever(ratesMap).thenReturn(null)
        }
        whenever(localRepository.getSavedExchangeRate()).thenReturn(AppResult.Success(savedExchangeRateWithNull))
        assertNull(useCase.invoke(emptyMap))

        whenever(localRepository.getSavedExchangeRate()).thenReturn(AppResult.Success(null))
        assertNull(useCase.invoke(emptyMap))

        val testWithEmptyMap = mock<ExchangeRate>().apply {
            whenever(ratesMap).thenReturn(emptyMap())
        }
        whenever(localRepository.getSavedExchangeRate()).thenReturn(AppResult.Success(testWithEmptyMap))
        assertNull(useCase.invoke(emptyMap))
    }

    @Test
    fun invoke_whenCacheReturnsErrorAndGivenMapIsEmpty_returnsNull() = runTest {
        whenever(localRepository.getSavedExchangeRate()).thenReturn(AppResult.Error(500, "Unknown Error"))

        val list = useCase.invoke(emptyMap)

        assertNull(list)
    }
}

package com.sample.currencyconversion.core.data.repository

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.freature.converter.data.mapper.CurrencyMapper
import com.sample.currencyconversion.freature.converter.data.repository.LocalRepositoryImpl
import com.sample.currencyconversion.freature.converter.data.source.local.LocalDataSource
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(JUnit4::class)
class LocalRepositoryTest {

    private lateinit var localRepository: LocalRepository
    private val localDataSource: LocalDataSource = Mockito.mock()

    @Before
    fun setUp() {
        localRepository = LocalRepositoryImpl(localDataSource)
    }

    @Test
    fun getSavedExchangeRate_whenCachedDataExists_returnsSuccess() = runTest {
        val flow = flowOf(TestHelper.CACHED_EXCHANGE_RATE)
        whenever(localDataSource.getSavedExchangeRateJson()).thenReturn(flow)

        val expected = CurrencyMapper.toExchangeRate(TestHelper.CACHED_EXCHANGE_RATE)
        val result = localRepository.getSavedExchangeRate()
        assertEquals(AppResult.Success(expected), result)
    }

    @Test
    fun getSavedExchangeRate_whenCachedDataIsNull_returnsError() = runTest {
        val flow = flowOf(null)
        whenever(localDataSource.getSavedExchangeRateJson()).thenReturn(flow)

        val result = localRepository.getSavedExchangeRate()
        assertEquals(AppResult.Error(404, "No cached exchange rate found"), result)

        val expected = CurrencyMapper.toExchangeRate(null)
        assertNull(expected)
    }

    @Test
    fun getSavedExchangeRate_whenCachedDataIsEmptyObject_returnsDefaultExchangeRate() = runTest {
        val invalidCached = "{}"
        val flow = flowOf(invalidCached)
        whenever(localDataSource.getSavedExchangeRateJson()).thenReturn(flow)

        val result = localRepository.getSavedExchangeRate()
        assertEquals(AppResult.Success(ExchangeRate()), result)
    }

    @Test
    fun getSavedExchangeRate_whenCachedDataIsInvalidJson_throwsJsonSyntaxException() = runTest {
        val invalidJson = "!#$@#$%"
        val flow = flowOf(invalidJson)
        whenever(localDataSource.getSavedExchangeRateJson()).thenReturn(flow)

        val exception = assertThrows(JsonSyntaxException::class.java) {
            Gson().fromJson(invalidJson, ExchangeRate::class.java)
        }
        assert(exception.message!!.contains("Expected BEGIN_OBJECT"))
    }

    @Test
    fun saveExchangeRateJobState_whenTruePassed_returnsTrue() = runTest {
        whenever(localDataSource.saveExchangeRateJobState(true)).thenReturn(true)

        val result = localRepository.saveExchangeRateJobState(true)

        assertTrue(result)
        verify(localDataSource).saveExchangeRateJobState(true)
    }

    @Test
    fun saveExchangeRateJobState_whenFalsePassed_returnsFalse() = runTest {
        whenever(localDataSource.saveExchangeRateJobState(false)).thenReturn(false)

        val result = localRepository.saveExchangeRateJobState(false)

        assertFalse(result)
        verify(localDataSource).saveExchangeRateJobState(false)
    }

    @Test
    fun isExchangeRateJobExecuted_whenExecuted_returnsTrue() = runTest {
        whenever(localDataSource.isExchangeRateJobExecuted()).thenReturn(flowOf(true))

        val result = localRepository.isExchangeRateJobExecuted()

        assertTrue(result == true)
        verify(localDataSource).isExchangeRateJobExecuted()
    }

    @Test
    fun isExchangeRateJobExecuted_whenNotExecuted_returnsFalse() = runTest {
        whenever(localDataSource.isExchangeRateJobExecuted()).thenReturn(flowOf(false))

        val result = localRepository.isExchangeRateJobExecuted()

        assertTrue(result == false)
        verify(localDataSource).isExchangeRateJobExecuted()
    }

    @Test
    fun saveExchangeRateResponse_whenValidData_returnsTrue() = runTest {
        val exchangeRate = ExchangeRate(
            disclaimer = "Test disclaimer",
            license = "Test license",
            timestamp = 123L,
            base = "USD",
            ratesMap = mapOf("EUR" to 1.1)
        )
        val json = Gson().toJson(exchangeRate)

        whenever(localDataSource.saveExchangeRate(json)).thenReturn(true)

        val result = localRepository.saveExchangeRateResponse(exchangeRate)

        assertTrue(result)
        verify(localDataSource).saveExchangeRate(json)
    }

    @Test
    fun saveExchangeRateResponse_whenNullData_returnsFalse() = runTest {
        val exchangeRate: ExchangeRate? = null
        val json = Gson().toJson(exchangeRate)

        whenever(localDataSource.saveExchangeRate(json)).thenReturn(false)

        val result = localRepository.saveExchangeRateResponse(exchangeRate)

        assertFalse(result)
        verify(localDataSource).saveExchangeRate(json)
    }

    @Test
    fun getCurrencyListFromMap_whenMapIsNull_returnsNull() {
        val result = localRepository.getCurrencyListFromMap(null)
        assertNull(result)
    }

    @Test
    fun getCurrencyListFromMap_whenMapIsValid_returnsMappedList() {
        val map = mapOf("USD" to 1.0, "EUR" to 0.9)
        val expectedList = CurrencyMapper.toList(map)

        val result = localRepository.getCurrencyListFromMap(map)
        assertEquals(expectedList, result)
    }
}

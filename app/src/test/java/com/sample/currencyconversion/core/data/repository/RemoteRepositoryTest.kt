package com.sample.currencyconversion.core.data.repository

import com.sample.currencyconversion.common.mapper.AppResult
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.freature.converter.data.repository.RemoteRepositoryImpl
import com.sample.currencyconversion.freature.converter.data.source.remote.RemoteDataSource
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import com.sample.currencyconversion.helper.TestHelper
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
@RunWith(JUnit4::class)
class RemoteRepositoryTest {

    private lateinit var exchangeRatesRepository: RemoteRepository
    private val exchangeRate: ExchangeRate = mock()
    private val exchangeRatesDataSource: RemoteDataSource = mock()

    @Before
    fun setup() = runTest {
        exchangeRatesRepository = RemoteRepositoryImpl(exchangeRatesDataSource)
    }

    @Test
    fun getExchangeRatesList_whenApiReturnsSuccess_returnsSuccessResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(TestHelper.VALID_APP_ID))
            .thenReturn(ApiResponse.Success(exchangeRate))

        val result = exchangeRatesRepository.getExchangeRatesList(TestHelper.VALID_APP_ID)

        assert(result is AppResult.Success)
        assertEquals(exchangeRate, (result as AppResult.Success).data)
    }

    @Test
    fun getExchangeRatesList_whenApiReturnsSuccessWithNull_returnsSuccessWithNullData() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(TestHelper.VALID_APP_ID))
            .thenReturn(ApiResponse.Success(null))

        val result = exchangeRatesRepository.getExchangeRatesList(TestHelper.VALID_APP_ID)

        assert(result is AppResult.Success)
        assertEquals(null, (result as AppResult.Success).data)
    }

    @Test
    fun getExchangeRatesList_whenApiReturnsError_returnsErrorResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(TestHelper.INVALID_APP_ID))
            .thenReturn(ApiResponse.Error(401, "Unauthorized"))

        val result = exchangeRatesRepository.getExchangeRatesList(TestHelper.INVALID_APP_ID)

        assert(result is AppResult.Error)
        result as AppResult.Error
        assertEquals(401, result.code)
        assertEquals("Unauthorized", result.message)
    }

    @Test
    fun getExchangeRatesList_whenApiThrowsException_returnsExceptionResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(""))
            .thenReturn(ApiResponse.Exception(Throwable("Unknown Error")))

        val result = exchangeRatesRepository.getExchangeRatesList("")

        assert(result is AppResult.Exception)
        result as AppResult.Exception
        assertEquals("Unknown Error", result.throwable.message)
    }

    @Test
    fun getExchangeRateInBackground_whenApiReturnsSuccess_returnsSuccessResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(TestHelper.VALID_APP_ID))
            .thenReturn(ApiResponse.Success(exchangeRate))

        val result = exchangeRatesRepository.getExchangeRateInBackground(TestHelper.VALID_APP_ID, this)

        assert(result is ApiResponse.Success)
        assertEquals(exchangeRate, (result as ApiResponse.Success).data)
    }

    @Test
    fun getExchangeRateInBackground_whenApiReturnsError_returnsErrorResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(TestHelper.INVALID_APP_ID))
            .thenReturn(ApiResponse.Error(401, "Unauthorized"))

        val result = exchangeRatesRepository.getExchangeRateInBackground(TestHelper.INVALID_APP_ID, this)

        assert(result is ApiResponse.Error)
        result as ApiResponse.Error
        assertEquals(401, result.code)
        assertEquals("Unauthorized", result.message)
    }

    @Test
    fun getExchangeRateInBackground_whenApiThrowsException_returnsExceptionResult() = runTest {
        whenever(exchangeRatesDataSource.fetchExchangeRates(""))
            .thenReturn(ApiResponse.Exception(Throwable("Unknown Error")))

        val result = exchangeRatesRepository.getExchangeRateInBackground("", this)

        assert(result is ApiResponse.Exception)
        result as ApiResponse.Exception
        assertEquals("Unknown Error", result.throwable.message)
    }
}




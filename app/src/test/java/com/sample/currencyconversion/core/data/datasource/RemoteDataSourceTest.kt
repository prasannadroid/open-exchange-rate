package com.sample.currencyconversion.core.data.datasource

import com.sample.currencyconversion.core.data.api.ApiService
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.freature.converter.data.dto.ExchangeRatesDto
import com.sample.currencyconversion.freature.converter.data.source.remote.RemoteDataSource
import com.sample.currencyconversion.helper.TestHelper
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertNull
@RunWith(JUnit4::class)
class RemoteDataSourceTest {

    private lateinit var apiService: ApiService
    private lateinit var exchangeRatesDataSource: RemoteDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var response: MockResponse

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

        response = MockResponse()

        exchangeRatesDataSource = RemoteDataSource(apiService)
    }

    @Test
    fun fetchExchangeRates_whenResponseIsSuccess_returnsValidData() = runTest {
        // Arrange
        val content = TestHelper.readFileResponse("/response_200.json")
        response.setBody(content)
        response.setResponseCode(200)
        mockWebServer.enqueue(response)

        // Act
        val result = exchangeRatesDataSource.fetchExchangeRates(TestHelper.VALID_URL)
        mockWebServer.takeRequest()

        // Assert
        assert(result is ApiResponse.Success)
        assert((result as ApiResponse.Success).data != null)
        assert(result.data!!.base == "USD")
        assert(result.data!!.ratesMap["AED"] == 3.673)
        assert(result.data!!.ratesMap["AFN"] == 72.268373)
    }

    @Test
    fun fetchExchangeRates_whenUnauthorized_returnsErrorResponse() = runTest {
        // Arrange
        response.setResponseCode(401)
        mockWebServer.enqueue(response)

        // Act
        val result = exchangeRatesDataSource.fetchExchangeRates(TestHelper.INVALID_URL)
        mockWebServer.takeRequest()

        // Assert
        assert(result is ApiResponse.Error)
    }

    @Test
    fun fetchExchangeRates_whenResponseBodyIsNull_returnsSuccessWithNullData() = runTest {
        // Arrange
        val apiService = mock(ApiService::class.java)
        val remoteDataSource = RemoteDataSource(apiService)

        val nullResponse = Response.success<ExchangeRatesDto>(null)
        whenever(apiService.fetchExchangeRates("test_app_id")).thenReturn(nullResponse)

        // Act
        val result = remoteDataSource.fetchExchangeRates("test_app_id")

        // Assert
        assertTrue(result is ApiResponse.Success)
        assertNull((result as ApiResponse.Success).data)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

package com.sample.currencyconversion.core.data.remote

import com.sample.currencyconversion.core.data.api.ApiService
import com.sample.currencyconversion.freature.converter.data.source.remote.RemoteDataSource
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {

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
    fun fetchExchangeRates_whenResponseIsSuccess_returnsStatus200WithBody() = runTest {
        // Arrange
        val content = TestHelper.readFileResponse("/response_200.json")
        response.setBody(content)
        response.setResponseCode(200)
        mockWebServer.enqueue(response)

        // Act
        val response = apiService.fetchExchangeRates(TestHelper.VALID_URL)
        mockWebServer.takeRequest()

        // Assert
        assertThat(response.body().toString().isNotEmpty(), `is`(true))
        Assert.assertEquals(200, response.code())
        assertThat(response.body()?.ratesList?.isNotEmpty(), `is`(true))
    }

    @Test
    fun fetchExchangeRates_whenUnauthorized_returns401WithEmptyBody() = runTest {
        // Arrange
        response.setResponseCode(401)
        mockWebServer.enqueue(response)

        // Act
        val response = apiService.fetchExchangeRates(TestHelper.VALID_URL)
        mockWebServer.takeRequest()

        // Assert
        assertThat(response.isSuccessful, `is`(false))
        Assert.assertEquals(null, response.body())
        Assert.assertEquals(401, response.code())
        assertThat(response.message().toString(), `is`("Client Error"))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

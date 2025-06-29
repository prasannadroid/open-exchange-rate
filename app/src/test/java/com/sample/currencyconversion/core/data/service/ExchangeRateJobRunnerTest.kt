package com.sample.currencyconversion.core.data.service

import com.sample.currencyconversion.core.data.api.response.ApiResponse
import com.sample.currencyconversion.core.data.local.MyKeyProvider
import com.sample.currencyconversion.freature.converter.domain.model.ExchangeRate
import com.sample.currencyconversion.freature.converter.domain.repository.LocalRepository
import com.sample.currencyconversion.freature.converter.domain.repository.RemoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ExchangeRateJobRunnerTest {

    private lateinit var exchangeRateJobRunner: ExchangeRateJobRunner

    private val localRepository: LocalRepository = Mockito.mock()

    private val remoteRepository: RemoteRepository = Mockito.mock()

    private val myKeyProvider: MyKeyProvider = Mockito.mock()

    private val fakeExchangeRate: ExchangeRate = Mockito.mock()

    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        exchangeRateJobRunner =
            ExchangeRateJobRunner(remoteRepository, localRepository, myKeyProvider)
    }

    @Test
    fun runJob_ExchangeJobStateFail_SaveState_NotExecute() = runTest {
        // test status false
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(false)
        exchangeRateJobRunner.runJob()

        // test method execution
        verify(localRepository).saveExchangeRateJobState(true)
    }

    @Test
    fun runJob_ExchangeJobState_WhenNull() = runTest {
        // test status null
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(null)
        exchangeRateJobRunner.runJob()

        // test method execution
        verify(localRepository).saveExchangeRateJobState(true)
    }

    @Test
    fun runJob_ExchangeJobState_WhenTrue() = runTest {
        // test status null
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        exchangeRateJobRunner.runJob()

        // test method execution
        verify(localRepository, never()).saveExchangeRateJobState(true)
    }

    @Test
    fun runJob_shouldSaveResponse_onApiResponseSuccess() = runTest(testDispatcher) {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn("test-key")
        whenever(remoteRepository.getExchangeRateInBackground(eq("test-key"), any()))
            .thenReturn(ApiResponse.Success(fakeExchangeRate))
        whenever(localRepository.saveExchangeRateResponse(any()))
            .thenReturn(true)

        exchangeRateJobRunner.runJob()
        advanceUntilIdle()

        verify(localRepository).saveExchangeRateResponse(fakeExchangeRate)
    }


    @Test
    fun runJon_shouldNotSaveResponse_whenResponseNull() = runTest(testDispatcher) {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn("test-key")
        whenever(remoteRepository.getExchangeRateInBackground(eq("test-key"), any()))
            .thenReturn(ApiResponse.Success(null))
        whenever(localRepository.saveExchangeRateResponse(any()))
            .thenReturn(true)

        exchangeRateJobRunner.runJob()
        advanceUntilIdle()

        verify(localRepository, never()).saveExchangeRateResponse(fakeExchangeRate)
    }


    @Test
    fun runJon_shouldLogOn_ApiResponseError() = runTest(testDispatcher) {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn("test-key")
        whenever(remoteRepository.getExchangeRateInBackground(eq("test-key"), any()))
            .thenReturn(ApiResponse.Error(500, "Network error"))

        exchangeRateJobRunner.runJob()
        advanceUntilIdle()

        verify(localRepository, never()).saveExchangeRateResponse(any())
    }

    @Test
    fun runJob_shouldLogOn_ApiResponseException() = runTest(testDispatcher) {
        whenever(localRepository.isExchangeRateJobExecuted()).thenReturn(true)
        whenever(myKeyProvider.readFromPropertiesFile()).thenReturn("test-key")
        whenever(remoteRepository.getExchangeRateInBackground(eq("test-key"), any()))
            .thenReturn(ApiResponse.Exception(RuntimeException("Timeout")))

        exchangeRateJobRunner.runJob()
        advanceUntilIdle()

        verify(localRepository, never()).saveExchangeRateResponse(any())
    }
}

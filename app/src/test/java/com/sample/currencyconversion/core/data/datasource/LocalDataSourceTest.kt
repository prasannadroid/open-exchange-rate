package com.sample.currencyconversion.core.data.datasource

import com.sample.currencyconversion.core.data.local.MyDataStore
import com.sample.currencyconversion.freature.converter.data.source.local.LocalDataSource
import com.sample.currencyconversion.helper.TestHelper
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue
@RunWith(JUnit4::class)
class LocalDataSourceTest {

    private val myDataStore: MyDataStore = Mockito.mock()
    private lateinit var localDataStore: LocalDataSource

    @Before
    fun setup() {
        localDataStore = LocalDataSource(myDataStore)
    }

    @Test
    fun saveExchangeRate_whenValidData_returnsTrue() = runTest {
        whenever(myDataStore.saveExchangeRate(TestHelper.CACHED_EXCHANGE_RATE)).thenReturn(true)

        val result = localDataStore.saveExchangeRate(TestHelper.CACHED_EXCHANGE_RATE)
        assertTrue(result)
    }

    @Test
    fun saveExchangeRate_whenNullData_returnsFalse() = runTest {
        whenever(myDataStore.saveExchangeRate(null)).thenReturn(false)

        val result = localDataStore.saveExchangeRate(null)
        assertFalse(result)
    }

    @Test
    fun getSavedExchangeRateJson_whenDataExists_returnsFlow() = runTest {
        val flow = flowOf(TestHelper.CACHED_EXCHANGE_RATE)
        whenever(myDataStore.getSavedExchangeRateJson()).thenReturn(flow)

        val result = localDataStore.getSavedExchangeRateJson()
        assertEquals(result, flow)
    }

    @Test
    fun getSavedExchangeRateJson_whenNoData_returnsNullFlow() = runTest {
        val flow = flowOf(null)
        whenever(myDataStore.getSavedExchangeRateJson()).thenReturn(flow)

        val result = localDataStore.getSavedExchangeRateJson()
        assertEquals(result, flow)
    }

    @Test
    fun clearExchangeRateData_whenSuccess_returnsTrue() = runTest {
        whenever(myDataStore.clearApiResponse()).thenReturn(true)

        val result = localDataStore.clearExchangeRateData()
        assertTrue(result)
    }

    @Test
    fun clearExchangeRateData_whenFailure_returnsFalse() = runTest {
        whenever(myDataStore.clearApiResponse()).thenReturn(false)

        val result = localDataStore.clearExchangeRateData()
        assertFalse(result)
    }

    @Test
    fun isExchangeRateJobExecuted_whenExecuted_returnsTrueFlow() = runTest {
        val flow = flowOf(true)
        whenever(myDataStore.isExchangeRateJobExecuted()).thenReturn(flow)

        val result = localDataStore.isExchangeRateJobExecuted()
        assertEquals(result, flow)
    }

    @Test
    fun isExchangeRateJobExecuted_whenNotExecuted_returnsFalseFlow() = runTest {
        val flow = flowOf(false)
        whenever(myDataStore.isExchangeRateJobExecuted()).thenReturn(flow)

        val result = localDataStore.isExchangeRateJobExecuted()
        assertEquals(result, flow)
    }

    @Test
    fun saveExchangeRateJobState_whenTrue_returnsTrue() = runTest {
        whenever(myDataStore.saveExchangeRateJobState(true)).thenReturn(true)

        val result = localDataStore.saveExchangeRateJobState(true)
        assertTrue(result)
    }

    @Test
    fun saveExchangeRateJobState_whenFalse_returnsFalse() = runTest {
        whenever(myDataStore.saveExchangeRateJobState(false)).thenReturn(false)

        val result = localDataStore.saveExchangeRateJobState(false)
        assertFalse(result)
    }
}

package com.sample.currencyconversion.freature.converter.data.source.local

import com.sample.currencyconversion.core.data.local.MyDataStore
import kotlinx.coroutines.flow.Flow

// A data source class that interacts with local storage using MyDataStore
class LocalDataSource(private val myDataStore: MyDataStore) {

    // Saves the exchange rate JSON string to the DataStore
    suspend fun saveExchangeRate(jsonData: String?) = myDataStore.saveExchangeRate(jsonData)

    // Retrieves the saved exchange rate JSON string from the DataStore as a Flow
    fun getSavedExchangeRateJson(): Flow<String?> {
        return myDataStore.getSavedExchangeRateJson()
    }

    // Clears the saved exchange rate data from the DataStore
    suspend fun clearExchangeRateData() = myDataStore.clearApiResponse()

    // Returns a Flow that emits the status of whether the exchange rate fetch job has been executed
    fun isExchangeRateJobExecuted(): Flow<Boolean?> {
        return myDataStore.isExchangeRateJobExecuted()
    }

    // Saves the job state (true if executed, false otherwise) to the DataStore
    suspend fun saveExchangeRateJobState(jobState: Boolean) =
        myDataStore.saveExchangeRateJobState(jobState)
}
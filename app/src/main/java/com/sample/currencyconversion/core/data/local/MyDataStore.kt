package com.sample.currencyconversion.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class MyDataStore(private val dataStore: DataStore<Preferences>) {

    suspend fun saveExchangeRate(jsonData: String?): Boolean {
        // return false if null or blank
        if (jsonData.isNullOrBlank()) return false

        return runCatching {
            // save data to preferences
            dataStore.edit { preferences ->
                preferences[EXCHANGE_RATE_JSON] = jsonData
            }
        }.onFailure { e ->
            e.printStackTrace()
        }.isSuccess
    }

    // display language instruction saved state
    fun getSavedExchangeRateJson() = dataStore.data.map { preferences ->
        preferences[EXCHANGE_RATE_JSON]
    }

    // clear saved data
    suspend fun clearApiResponse() = runCatching {
        dataStore.edit {
            it.clear()
        }
    }.onFailure { e ->
        e.printStackTrace()
    }.isSuccess

    // use to pre
    fun isExchangeRateJobExecuted() = dataStore.data.map { preferences ->
        preferences[EXCHANGE_RATE_JOB_STATE]
    }

    // save job state
    suspend fun saveExchangeRateJobState(jobState: Boolean) = runCatching {
        // save
        dataStore.edit { preferences ->
            preferences[EXCHANGE_RATE_JOB_STATE] = jobState
        }
    }.onFailure { e ->
        e.printStackTrace()
    }.isSuccess

    // holds the Data Store keys
    companion object {
        // data store keys
        val EXCHANGE_RATE_JSON = stringPreferencesKey("EXCHANGE_RATE_JSON")
        val EXCHANGE_RATE_JOB_STATE = booleanPreferencesKey("EXCHANGE_RATE_JOB_STATE")

    }
}
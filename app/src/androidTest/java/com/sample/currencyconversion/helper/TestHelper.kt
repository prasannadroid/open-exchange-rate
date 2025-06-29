package com.sample.currencyconversion.helper

import android.content.Context
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.closeTo

class TestHelper {
    companion object {
        const val testDataStoreName = "testDataStoreName"

        private var dataStore: DataStore<Preferences>? = null

        fun getDataStore(context: Context): DataStore<Preferences> {
            return dataStore ?: PreferenceDataStoreFactory.create(
                produceFile = {
                    context.preferencesDataStoreFile(testDataStoreName)
                }
            ).also { dataStore = it }
        }

        private fun makeDoubleFormat(nodeValue: Any?): Double {
            var currencyValue = 0.0
            nodeValue?.let {
                currencyValue = it.toString().replace("[", "").replace("]", "").toDouble()

            }
            return currencyValue
        }

        private fun makeStringFormat(nodeValue: Any?): String {
            var currencyCode = ""
            nodeValue?.let {
                currencyCode = it.toString().replace("[", "").replace("]", "")

            }
            return currencyCode
        }

        fun getTextFromSemantic(semanticCollection: SemanticsNodeInteraction): String {

            var formattedText = ""

            for ((key, value) in semanticCollection.fetchSemanticsNode().config) {

                if (key.name == "Text") {
                    // removing [ ] characters and return string
                    formattedText = makeStringFormat(value)
                }
            }
            return formattedText
        }

        fun getNumberFromSemantic(semanticCollection: SemanticsNodeInteraction): Double {
            var formattedNumber = 0.0

            for ((key, value) in semanticCollection.fetchSemanticsNode().config) {

                if (key.name == "Text") {
                    // removed [ ] characters and get double value
                    formattedNumber = makeDoubleFormat(value)
                }
            }
            return formattedNumber
        }

        fun assertCloseTo(actual: Double, matcher: Double) {
            // check the different
            val diff = if (actual < matcher) matcher - actual else actual - matcher
            assertThat(actual, closeTo(matcher, diff)) // Using equalTo matcher

        }


    }
}
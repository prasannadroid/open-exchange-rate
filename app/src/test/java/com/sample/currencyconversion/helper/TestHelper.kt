package com.sample.currencyconversion.helper

import java.io.InputStreamReader

class TestHelper {

    companion object {

        const val VALID_URL = "validUrl"

        const val INVALID_URL = "invalidUrl"

        const val VALID_APP_ID = "validAppId"

        const val INVALID_APP_ID = "INVALID_APP_ID"

        const val CACHED_EXCHANGE_RATE = "{\n" +
                "  \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                "  \"license\": \"https://openexchangerates.org/license\",\n" +
                "  \"timestamp\": 1750359600,\n" +
                "  \"base\": \"USD\",\n" +
                "  \"ratesMap\": {\n" +
                "    \"EUR\": 0.85,\n" +
                "    \"JPY\": 110.12,\n" +
                "    \"LKR\": 300.55\n" +
                "  }\n" +
                "}"

        /**
         * Reads the contents of a json file located in the resources directory and returns it as a single json string.
         *
         * @param fileName The name of the file to be read. text formal will be filename.json
         * @return The contents of the file as a single string.
         */
        fun readFileResponse(fileName: String): String {
            val inputStream = TestHelper::class.java.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        }
    }
}
package com.sample.currencyconversion.core.exception

import com.google.gson.JsonParseException
import com.sample.currencyconversion.core.data.api.response.ApiResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun handleException(e: Throwable): ApiResponse.Error {
    // print full stack trace here
    e.printStackTrace()
    return when (e) {
        is SocketTimeoutException -> ApiResponse.Error(504, "Request timed out")
        is UnknownHostException -> ApiResponse.Error(503, "No internet")
        is ConnectException -> ApiResponse.Error(502, "Connection failed")
        is HttpException -> ApiResponse.Error(e.code(), "HTTP error: ${e.message()}")
        is JsonParseException -> ApiResponse.Error(400, "Parsing error")
        is IOException -> ApiResponse.Error(503, "Network I/O error")
        else -> ApiResponse.Error(500, e.localizedMessage ?: "Unknown error")
    }
}
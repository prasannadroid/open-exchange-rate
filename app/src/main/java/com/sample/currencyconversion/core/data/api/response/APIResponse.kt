package com.sample.currencyconversion.core.data.api.response

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class Error(val code: Int, val message: String?) : ApiResponse<Nothing>()
    data class Exception(val throwable: Throwable) : ApiResponse<Nothing>()

}

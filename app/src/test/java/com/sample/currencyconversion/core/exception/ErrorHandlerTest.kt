package com.sample.currencyconversion.core.exception

import com.google.gson.JsonParseException
import junit.framework.TestCase.assertEquals
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.Test

class ErrorHandlerTest {

    @Test
    fun test_handleException_socketTimeout() {
        val result = handleException(SocketTimeoutException())
        assertEquals(504, result.code)
        assertEquals("Request timed out", result.message)
    }

    @Test
    fun test_handleException_unknownHost() {
        val result = handleException(UnknownHostException())
        assertEquals(503, result.code)
        assertEquals("No internet", result.message)
    }

    @Test
    fun test_handleException_connectException() {
        val result = handleException(ConnectException())
        assertEquals(502, result.code)
        assertEquals("Connection failed", result.message)
    }

    @Test
    fun test_handleException_httpException() {
        // Create a fake HttpException with code 403
        val httpException = mockHttpException(403, "Forbidden")
        val result = handleException(httpException)
        assertEquals(403, result.code)
        assertEquals("HTTP error: Forbidden", result.message)
    }

    @Test
    fun test_handleException_jsonParseException() {
        val result = handleException(JsonParseException("Invalid JSON"))
        assertEquals(400, result.code)
        assertEquals("Parsing error", result.message)
    }

    @Test
    fun test_handleException_ioException() {
        val result = handleException(IOException("Disk error"))
        assertEquals(503, result.code)
        assertEquals("Network I/O error", result.message)
    }

    @Test
    fun test_handleException_unknownException() {
        val result = handleException(IllegalStateException("Unexpected state"))
        assertEquals(500, result.code)
        assertEquals("Unexpected state", result.message)
    }

    // Helper to mock HttpException
    private fun mockHttpException(code: Int, message: String): HttpException {
        val response = retrofit2.Response.error<String>(
            code,
            okhttp3.ResponseBody.create(null, "")
        )
        return object : HttpException(response) {
            override fun message(): String = message
        }
    }
}
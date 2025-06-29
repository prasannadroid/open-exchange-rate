package com.sample.currencyconversion.helper

import com.sample.currencyconversion.core.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatcherProvider(
    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
) : CoroutineDispatcherProvider {
    override val io: CoroutineDispatcher = testDispatcher
    override val main: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
}

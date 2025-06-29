package com.sample.currencyconversion.core

import com.sample.currencyconversion.core.extention.toTwoDecimals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class CoreTest {

    @Test
    fun test_toTwoDecimals_correctFormatting() {
        assertEquals("12.35", 12.345.toTwoDecimals())
        assertEquals("12.34", 12.344.toTwoDecimals()) // rounding down
        assertEquals("0.00", 0.0.toTwoDecimals())
        assertEquals("100.00", 100.0.toTwoDecimals())
    }
}
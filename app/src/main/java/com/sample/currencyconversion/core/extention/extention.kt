package com.sample.currencyconversion.core.extention

// make double values to two decimal points when display in UI
fun Double.toTwoDecimals() = "%.2f".format(this)
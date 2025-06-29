package com.sample.currencyconversion.common.ui.state

import com.sample.currencyconversion.core.data.api.apistate.ProgressState

data class UiState(
    val isLoading: ProgressState = ProgressState.IDLE,
    val errorCode: Int? = null,
    val message: String? = null
)
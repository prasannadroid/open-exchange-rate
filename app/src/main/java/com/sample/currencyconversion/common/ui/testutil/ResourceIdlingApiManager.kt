package com.sample.currencyconversion.common.ui.testutil

import com.sample.currencyconversion.core.data.api.apistate.ProgressState
import com.sample.currencyconversion.common.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ResourceIdlingApiManager {

    companion object {
        /**
         * based api loading state manage the resource idling increment and decrement
         */
        suspend fun resourceIdlingForApi(uiStateStateFlow: Flow<UiState>) {
            var started = false

            uiStateStateFlow
                .map { it.isLoading }
                .distinctUntilChanged()
                .collect { state ->
                    when {
                        state == ProgressState.STARTED -> {
                            ResourceIdleManager.increment()
                            started = true
                        }

                        started && state == ProgressState.STOP -> {
                            ResourceIdleManager.decrement()
                            return@collect
                        }

                        started && state == ProgressState.IDLE -> {
                            ResourceIdleManager.decrement()
                            return@collect
                        }

                    }
                }
        }

    }
}
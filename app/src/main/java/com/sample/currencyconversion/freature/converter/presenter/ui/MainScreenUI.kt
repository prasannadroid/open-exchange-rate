package com.sample.currencyconversion.freature.converter.presenter.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.currencyconversion.core.data.api.apistate.ProgressState
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.ExchangeRateViewModel
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun MainScreenUI(
    exchangeRateViewModel: ExchangeRateViewModel = koinViewModel()
) {

    val context = LocalContext.current

    // get ui state from view model flow
    val uiState by exchangeRateViewModel.uiStateStateFlow.collectAsStateWithLifecycle()

    // get the converted currency list
    val currencyList by exchangeRateViewModel.currencyListStateFlow.collectAsStateWithLifecycle()

    // initial call
    LaunchedEffect(Unit) {
        exchangeRateViewModel.getExchangeRates()
    }

    LaunchedEffect(uiState) {
        // check the api call finished
        if (uiState.isLoading == ProgressState.STOP) {
            // get the currency list from api or cache
            exchangeRateViewModel.getArrayListFromCurrencyMap()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.errorCode != null) {
            Toast.makeText(context, "${uiState.errorCode}: ${uiState.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize(), content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                // show the exchange rate content
                ExchangeRateContent(currencyList, uiState)
            }
        })

}


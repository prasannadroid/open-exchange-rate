package com.sample.currencyconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.sample.currencyconversion.core.data.scheduler.MyScheduler
import com.sample.currencyconversion.common.ui.testutil.ResourceIdlingApiManager
import com.sample.currencyconversion.freature.converter.presenter.ui.MainScreenUI
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.ExchangeRateViewModel
import com.sample.currencyconversion.common.theme.CurrencyConversionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val exchangeRateViewModel by viewModel<ExchangeRateViewModel>()

    private val myScheduler: MyScheduler by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConversionTheme {
                // start all the screens here
                MainScreenUI()
            }

        }

        lifecycleScope.launch(Dispatchers.IO) {
            myScheduler.scheduleBackgroundJob()
        }

        lifecycleScope.launch(Dispatchers.Main.immediate) {
            ResourceIdlingApiManager.resourceIdlingForApi(exchangeRateViewModel.uiStateStateFlow)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // stop background data fetching
        myScheduler.cancelJob()
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MaterialTheme {
            MainScreenUI()
        }
    }
}

package com.sample.currencyconversion.freature.converter.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.currencyconversion.core.extention.toTwoDecimals
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.CurrencyCalculationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StaggeredGrid(
    currencyList: List<Currency>?
) {

    if (currencyList.isNullOrEmpty()) {
        Text(
            text = "No currencies found",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
        return
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, bottom = 10.dp),
        contentPadding = PaddingValues(0.dp),
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp

    ) {
        items(currencyList.size, key = { key -> currencyList[key].currencyCode }) {
            // Grid view cell to show currency details
            StaggeredCell(currency = currencyList[it])
        }
    }

}

@Composable
fun StaggeredCell(
    currency: Currency,
    currencyCalculationViewModel: CurrencyCalculationViewModel = koinViewModel()
) {

    val updateBaseCurrencyFlow by currencyCalculationViewModel.baseCurrencyValue.collectAsStateWithLifecycle()

    val updateUserInputCurrencyFlow by currencyCalculationViewModel.inputCurrencyValue.collectAsStateWithLifecycle()

    var updatedBaseCurrency by remember { mutableDoubleStateOf(0.0) }

    var updatedUserInputValue by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(updateBaseCurrencyFlow) {
        updatedBaseCurrency = updateBaseCurrencyFlow
    }

    LaunchedEffect(updateUserInputCurrencyFlow) {
        updatedUserInputValue = updateUserInputCurrencyFlow
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp))
            .fillMaxSize()
            .padding(start = 10.dp, top = 12.dp, bottom = 20.dp, end = 10.dp)
    ) {

        Text(
            text = currency.currencyCode,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = currencyCalculationViewModel.calculateExchangeRate(
                currency,
                updatedBaseCurrency,
                updatedUserInputValue
            )
                .toTwoDecimals(), // restrict for two decimal points
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(top = 3.dp)
                .semantics {
                    contentDescription = "currency value"
                },
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun StaggeredCellPreview() {
    MaterialTheme {
        StaggeredCell(
            currency = Currency(currencyCode = "USD", 1.0)
        )
    }
}

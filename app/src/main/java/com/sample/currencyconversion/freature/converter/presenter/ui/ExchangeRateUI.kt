package com.sample.currencyconversion.freature.converter.presenter.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.currencyconversion.R
import com.sample.currencyconversion.common.theme.Gray70
import com.sample.currencyconversion.common.theme.MyTheme
import com.sample.currencyconversion.common.theme.padding15
import com.sample.currencyconversion.common.theme.padding25
import com.sample.currencyconversion.common.ui.state.UiState
import com.sample.currencyconversion.common.ui.widget.ActionButton
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import com.sample.currencyconversion.freature.converter.presenter.viewmodel.CurrencyCalculationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateContent(
    currencyList:List<Currency>?,
    uiState:UiState,
    currencyCalculationViewModel: CurrencyCalculationViewModel = koinViewModel(),
) {

    val baseCurrencyCode by currencyCalculationViewModel.baseCurrencyCode.collectAsStateWithLifecycle()

    var bottomSheetState by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()

    val buttonEnableState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(currencyList) {
        buttonEnableState.value = currencyList?.isNotEmpty() == true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = padding15,
                top = padding25,
                end = padding15
            )
            .background(MaterialTheme.colorScheme.background),
    ) {

        InputCurrencyContent { rate ->
            val modifiedCurrencyValue = if (rate.isNotBlank()) rate.toDouble() else 0.0
            // update user entered value
            currencyCalculationViewModel.updateInputCurrencyValue(modifiedCurrencyValue)
        }

        ActionButton(
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.End),
            baseCurrencyCode,
            buttonEnableState.value,
            R.drawable.ic_expand
        ) {
            bottomSheetState = true
        }

        currencyList?.takeIf { it.isNotEmpty() }?.let { list ->
            StaggeredGrid(currencyList = list)
        } ?: run {
            // maybe show empty state or loading
            if (uiState.errorCode == null) {
                CenteredProgressBar()
            }
        }


        // bottom sheet section
        if (bottomSheetState) {

            ExchangeRateModalBottomSheet(currencyList, sheetState, {
                // onClose
                bottomSheetState = false
            }) { currency ->
                // update the selected currency code
                currencyCalculationViewModel.updateBaseCurrencyState(currency.currencyCode)
                // update the selected currency value
                currencyCalculationViewModel.updateBaseCurrencyValue(currency.currencyValue)
            }
        }
    }

}

@Composable
fun CenteredProgressBar() {
    Row(
        modifier = Modifier
            .fillMaxSize(), // Fill the entire available space
        horizontalArrangement = Arrangement.Center // Center elements horizontally
    ) {
        ProgressBar() // Call the composable for the progress bar
    }
}

@Composable
fun ProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(60.dp)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun InputCurrencyContent(onTextChange: (String) -> Unit) {

    var textValueState by remember { mutableStateOf(TextFieldValue("1")) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            style = MyTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            text = stringResource(id = R.string.exchange_rates),
        )

        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .border(BorderStroke(2.dp, Gray70), RoundedCornerShape(20.dp))
                .height(60.dp)

        ) {
            BasicTextField(
                value = textValueState,
                onValueChange = {
                    textValueState = it
                    onTextChange(textValueState.text)
                },

                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
                    .onFocusEvent { event ->
                        if (event.isFocused) {
                            textValueState = textValueState.copy(
                                selection = TextRange(textValueState.text.length)
                            ) // Set selection to the end
                        }
                    }
                    .align(Alignment.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun InputCurrencyContentPreview() {
    MaterialTheme {
        InputCurrencyContent {

        }
    }
}

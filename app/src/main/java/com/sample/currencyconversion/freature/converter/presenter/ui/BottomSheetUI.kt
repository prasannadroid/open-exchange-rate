package com.sample.currencyconversion.freature.converter.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sample.currencyconversion.freature.converter.domain.model.Currency
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateModalBottomSheet(
    currencyList: List<Currency>?,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onItemSelected: (Currency) -> Unit,

    ) {

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = { onDismiss() }) {
        val scope = rememberCoroutineScope()
        BottomSheetContent(currencyList) {
            scope.launch {
                sheetState.hide()
                onDismiss()
                onItemSelected(it)
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    currencyList: List<Currency>?,
    onItemSelect: (Currency) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        currencyList?.let {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(it) {
                    ListRaw(currency = it) { item ->
                        onItemSelect(item)
                    }
                }
            }
        }


    }
}

@Composable
fun ListRaw(currency: Currency, onCloseClick: (Currency) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable { onCloseClick(currency) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        Text(
            text = currency.currencyCode,
            modifier = Modifier
                .semantics {
                    contentDescription = "currency code"
                }
                .padding(end = 10.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

    }
}
package com.sample.currencyconversion.common.ui.widget

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    modifier: Modifier,
    label: String,
    isButtonEnabled: Boolean,
    iconId: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = isButtonEnabled,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary, // Set the background color
            contentColor = MaterialTheme.colorScheme.onPrimary, // Set the text color
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Text(
            modifier = Modifier
                .width(60.dp)
                .semantics {
                    contentDescription = "currency button"
                }, text = label, style = MaterialTheme.typography.labelLarge
        )
        Icon(
            modifier = Modifier
                .size(24.dp),
            imageVector = ImageVector.vectorResource(id = iconId),
            contentDescription = "Down arrow Icon"
        )
    }
}
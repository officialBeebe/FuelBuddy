package com.dylanbeebe.fuelbuddy.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ClickableIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
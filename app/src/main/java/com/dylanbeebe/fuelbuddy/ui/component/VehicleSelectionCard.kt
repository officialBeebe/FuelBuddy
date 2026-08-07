package com.dylanbeebe.fuelbuddy.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@Composable
fun VehicleSelectionCard(
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier.size(width = 400.dp, height = 100.dp),
        onClick = onClick,
        content = { content() }
    )
}
package com.dylanbeebe.fuelbuddy.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme

@Composable
fun ActionFAB(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(
    showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "ActionFabPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun ActionFABPreview() {
    FuelBuddyTheme {
        ActionFAB(
            icon = Icons.Default.Add,
            contentDescription = "Add vehicle",
            onClick = {}
        )
    }
}
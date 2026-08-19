package com.dylanbeebe.fuelbuddy.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun MileageCard(
    mileage: Mileage,
    onClick: (Mileage) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        onClick = { onClick(mileage) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val date = Instant.parse(mileage.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.headlineMedium
                )
                ExportStatusBadge(isExported = mileage.isExported)
            }

            Row(
                modifier = Modifier.padding(16.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "%.2f gallons".format(mileage.volumeGallons),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$%.2f".format(mileage.totalDollars),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (mileage.isFullTank) "Full tank" else "Partial fill",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ExportStatusBadge(isExported: Boolean) {
    val (icon, label, tint) = if (isExported) {
        Triple(Icons.Filled.Check, "Exported", MaterialTheme.colorScheme.primary)
    } else {
        Triple(Icons.Filled.Warning, "Pending", MaterialTheme.colorScheme.error)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = tint
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "MileageCardPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun MileageCardPreview() {
    val testMileage = Mileage(
        mileageID = "test-mileage-uuid",
        timestamp = Instant.now().toString(),
        odometerMiles = 80085.69,
        volumeGallons = 6.9,
        isFullTank = true,
        fuelType = FuelType.REGULAR,
        totalDollars = 19.84,
        journal = "This is a test mileage log.",
        vehicle = "test-vehicle-uuid"
    )
    FuelBuddyTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MileageCard(
                mileage = testMileage,
                onClick = {}
            )
            MileageCard(
                mileage = testMileage,
                onClick = {}
            )
        }
    }
}
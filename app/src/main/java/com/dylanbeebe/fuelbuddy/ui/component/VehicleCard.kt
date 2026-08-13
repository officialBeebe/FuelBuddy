package com.dylanbeebe.fuelbuddy.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    onClick: (Vehicle) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(MaterialTheme.colorScheme.surfaceContainer),
        onClick = { onClick(vehicle) },
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                Text(
                    text = vehicle.nickname,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            Row {
                Text(
                    text = vehicle.plate ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview(
    showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "VehicleCardPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun VehicleCardPreview() {
    FuelBuddyTheme {
        VehicleCard(
            vehicle = Vehicle(
                vehicleID = "the-test-vehicle-uuid",
                nickname = "The Kia",
                make = "Kia",
                model = "Sorento",
                modelYear = 2015,
                plate = "ih8dis1",
            ),
            onClick = {}
        )
    }
}
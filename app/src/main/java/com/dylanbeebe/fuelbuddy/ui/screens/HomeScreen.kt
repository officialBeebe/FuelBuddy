package com.dylanbeebe.fuelbuddy.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.ui.VehicleViewModel
import com.dylanbeebe.fuelbuddy.ui.components.VehicleCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme

@Composable
fun HomeScreen(viewModel: VehicleViewModel = viewModel(), modifier: Modifier = Modifier) {
    /**
     * Home Screen
     *
     * Being the first screen the user encounters, the user's inventory of vehicles is displayed here along with button to add a new one.
     *
     * Example:
     *  _________________
     * /                 \
     * |    { The Kia }  |
     * |                 |
     * |     { Coop }    |
     * |                 |
     * | { Add Vehicle } |
     * \_________________/
     * */
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        vehicles = uiState.allVehicles,
        onAddVehicle = { /* navigate to add-vehicle screen */ },
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    vehicles: List<Vehicle>,
    onAddVehicle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(vehicles, key = { it.vehicleID }) { vehicle ->
            VehicleCard(vehicle = vehicle)
        }
        item {
            Button(onClick = onAddVehicle) {
                Text("Add Vehicle")
            }
        }
    }
}

@Preview(
    showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "HomeScreenPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun HomeScreenPreview() {
    FuelBuddyTheme {
        HomeScreenContent(
            vehicles = listOf(
                Vehicle(
                    nickname = "The Kia",
                    make = "Kia",
                    model = "Sorento",
                    modelYear = 2015,
                    plate = "ih8dis1",),
                Vehicle(
                    nickname = "Coop",
                    make = "BMW",
                    model = "Mini Cooper",
                    modelYear = 2016,
                    plate = "i<3dis1",),
            ),
            // TODO: Implement `AddVehicleScreen.kt`
            onAddVehicle = {},
        )
    }
}
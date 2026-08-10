package com.dylanbeebe.fuelbuddy.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.data.room.dao.MinimalVehicle
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.ui.component.AddVehicleCard
import com.dylanbeebe.fuelbuddy.ui.component.VehicleCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleListViewModel
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleListViewModelFactory

@Composable
fun HomeScreen(
    vehicleRepository: VehicleRepository, viewModel: VehicleListViewModel = viewModel(
        factory = VehicleListViewModelFactory (
            vehicleRepository
        )
    ), modifier: Modifier = Modifier
) {
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
        minimalVehicles = uiState.allVehicles,
        onAddVehicle = { /* TODO: navController.navigate("vehicle_add"} */ },
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    minimalVehicles: List<MinimalVehicle>,
    onAddVehicle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(minimalVehicles, key = { it.vehicleID }) { minimalVehicle ->
            VehicleCard(
                minimalVehicle = minimalVehicle,
                onClick = { /* TODO: navController.navigate("vehicle_edit/${it.vehicleID}") */ })
        }
        item {
            AddVehicleCard(onAddVehicle)
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
            minimalVehicles = listOf(
                MinimalVehicle(
                    vehicleID = "1234-5678-xxxx-oooo",
                    nickname = "The Kia",
                    plate = "ih8dis1",
                ),
                MinimalVehicle(
                    vehicleID = "oooo-xxxx-8765-4321",
                    nickname = "Coop",
                    plate = "i<3dis1",
                ),
            ),
            // TODO: Implement `AddVehicleScreen.kt`
            onAddVehicle = {},
        )
    }
}
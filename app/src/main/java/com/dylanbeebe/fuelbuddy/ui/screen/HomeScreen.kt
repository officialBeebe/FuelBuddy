package com.dylanbeebe.fuelbuddy.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.ui.component.AddVehicleButton
import com.dylanbeebe.fuelbuddy.ui.component.VehicleCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleListViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, onVehicleClick: (String) -> Unit, onAddVehicle: () -> Unit
) {
    /**
     * Home Screen
     *
     * Vehicle inventory is displayed here.
     *
     * Add new vehicle.
     *
     * Can launch vehicle screen by tapping on vehicle card.
     * */
    val vehicleListViewModel: VehicleListViewModel = viewModel(
        factory = VehicleListViewModel.Factory
    )
    val uiState by vehicleListViewModel.uiState.collectAsState()
    HomeScreenContent(
        vehicles = uiState,
        onAddVehicle = onAddVehicle,
        onVehicleClick = onVehicleClick,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    vehicles: List<Vehicle>,
    onVehicleClick: (String) -> Unit,
    onAddVehicle: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = null,
                modifier = Modifier
                    .alpha(0f)
                    .size(48.dp), // match other screens' "Go back" chevron to keep title padding consistent
            )
            // Title
            Text(
                text = "Inventory",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(vehicles, key = { it.vehicleID }) { vehicle ->
                    VehicleCard(
                        vehicle = vehicle, onClick = { onVehicleClick(it.vehicleID) })
                }
            }
        }
        AddVehicleButton(
            onAddVehicle,
            modifier = Modifier.align(Alignment.End))
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
                vehicleID = "the-test-vehicle-uuid",
                nickname = "The Kia",
                make = "Kia",
                model = "Sorento",
                modelYear = 2015,
                plate = "ih8dis1",
            ),
//                MinimalVehicle(
//                    vehicleID = "oooo-xxxx-8765-4321",
//                    nickname = "Coop",
//                    plate = "i<3dis1",
//                ),
        ), onVehicleClick = {}, onAddVehicle = {})
        // TODO: Implement `EditVehicleScreen.kt`
    }
}
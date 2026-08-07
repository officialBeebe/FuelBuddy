package com.dylanbeebe.fuelbuddy.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.data.room.dao.MinimalVehicle
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.ui.VehicleViewModel
import com.dylanbeebe.fuelbuddy.ui.VehicleViewModelFactory
import com.dylanbeebe.fuelbuddy.ui.component.VehicleCard
import com.dylanbeebe.fuelbuddy.ui.component.VehicleSelectionCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme

@Composable
fun HomeScreen(
    vehicleRepository: VehicleRepository, viewModel: VehicleViewModel = viewModel(
        factory = VehicleViewModelFactory(
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
        onAddVehicle = { /* navigate to add-vehicle screen */ },
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
            VehicleCard(minimalVehicle = minimalVehicle, onClick = {/* TODO: onNavigateVehicleScreen */})
        }
        item {
            AddVehicleCard(onAddVehicle)
        }
    }
}

@Composable
fun AddVehicleCard(
    onAddVehicle: () -> Unit,
    modifier: Modifier = Modifier
) {
    VehicleSelectionCard(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = onAddVehicle,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Vehicle",
                modifier = Modifier.size(32.dp)
            )
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
package com.dylanbeebe.fuelbuddy.ui.core.vehicle.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.component.MileageCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import java.time.Instant

@Composable
fun VehicleDetailScreen(
    modifier: Modifier = Modifier.Companion,
    onMileageClick: (String) -> Unit,
    onEditVehicle: (String) -> Unit,
    onBack: () -> Unit,
    onAddMileage: (String) -> Unit,
    onExportMileage: (String) -> Unit
) {
    /**
     * Vehicle Screen
     *
     * Vehicle details and mileage log.
     *
     * Add and export mileage.
     *
     * Can launch vehicle edit screen.
     * */
    val viewModel: VehicleDetailViewModel = viewModel(factory = VehicleDetailViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    VehicleScreenContent(
        vehicleWithAttachments = uiState.vehicle,
        vehicleMileage = uiState.mileage,
        onMileageClick = onMileageClick,
        onEditVehicle = onEditVehicle,
        onBack = onBack,
        onAddMileage = onAddMileage,
        onExportMileage = onExportMileage,
    )

}

@Composable
fun VehicleScreenContent(
    vehicleWithAttachments: VehicleWithAttachments?,
    vehicleMileage: List<Mileage>,
    onMileageClick: (String) -> Unit,
    onEditVehicle: (String) -> Unit,
    onBack: () -> Unit,
    onAddMileage: (String) -> Unit,
    onExportMileage: (String) -> Unit,
) {
    if (vehicleWithAttachments == null) {
        // Loading state — vehicle hasn't been emitted yet
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val vehicle = vehicleWithAttachments.vehicle
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // TODO: :START: Make composable: ScreenColumn
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Go back
                ClickableIcon(
                    icon = Icons.Filled.ChevronLeft,
                    contentDescription = "Go back",
                    onClick = { onBack() }
                )
                // Title
                Text(
                    text = vehicleWithAttachments.let { "\"${it?.vehicle?.nickname}\"" },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClickableIcon(
                    icon = Icons.Filled.Download,
                    contentDescription = "Download mileage export",
                    onClick = { onExportMileage(vehicle.vehicleID) },
                    modifier = Modifier.size(24.dp)
                )
                ClickableIcon(
                    icon = Icons.Filled.Edit,
                    contentDescription = "Edit vehicle details",
                    onClick = { onEditVehicle(vehicle.vehicleID) },
                    modifier = Modifier.size(24.dp)
                )
            }
        }

//        Row {
        // TODO: Average MPG, Average monthly gallons?, Average monthly miles?
//        }

        // TODO: :START: Make composable: CardSurface
//        Surface(
//            color = MaterialTheme.colorScheme.surfaceVariant,
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(vehicleMileage, key = { it.mileageID }) { mileage ->
                            MileageCard(
                                mileage = mileage, onClick = { onMileageClick(it.mileageID) })
                        }
                    }

                    // Add FAB
                    FloatingActionButton(
                        onClick = { onAddMileage(vehicle.vehicleID) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add mileage",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
        // TODO: :FINISH: Make composable: CardSurface
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Edit FAB
//            FloatingActionButton(
//                onClick = { onEditVehicle(vehicle.vehicleID) },
//                modifier = Modifier.size(64.dp),
//                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
//                containerColor = MaterialTheme.colorScheme.secondary,
//                contentColor = MaterialTheme.colorScheme.onSecondary,
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Edit vehicle",
//                    modifier = Modifier.size(32.dp)
//                )
//            }

            // Add FAB
//            FloatingActionButton(
//                onClick = { onAddMileage(vehicle.vehicleID) },
//                modifier = Modifier.size(64.dp),
//                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary,
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add mileage",
//                    modifier = Modifier.size(32.dp)
//                )
//            }
        }
    }
    // TODO: :FINISH: Make composable: ScreenColumn


//}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "VehicleScreenPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun VehicleScreenPreview() {
    val testVehicle = Vehicle(
        vehicleID = "the-test-vehicle-uuid",
        nickname = "The Kia",
        make = "Kia",
        model = "Sorento",
        modelYear = 2015,
        plate = "ih8dis1",
    )
    val testVehicleWithAttachments = VehicleWithAttachments(
        testVehicle, attachments = emptyList()
    )
    val testMileage = Mileage(
        mileageID = "the-test-mileage-uuid",
        timestamp = Instant.now().toString(),
        odometerMiles = 80085.69,
        volumeGallons = 6.9,
        isFullTank = true,
        fuelType = FuelType.REGULAR,
        totalDollars = 19.84,
        journal = "This is a test mileage log.",
        isExported = true,
        vehicleID = testVehicle.vehicleID
    )
    val testMileageList = listOf(testMileage)
    FuelBuddyTheme {
        VehicleScreenContent(
            vehicleWithAttachments = testVehicleWithAttachments, // match real constructor
            vehicleMileage = testMileageList,
            onEditVehicle = {},
            onMileageClick = {},
            onExportMileage = {},
            onBack = {},
            onAddMileage = {},
        )
    }
}
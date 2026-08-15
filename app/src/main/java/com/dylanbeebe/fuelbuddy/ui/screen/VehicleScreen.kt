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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.dylanbeebe.fuelbuddy.data.model.FuelType
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.component.MileageCard
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleDetailViewModel
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleMileageViewModel
import java.time.Instant

@Composable
fun VehicleScreen(
    modifier: Modifier = Modifier,
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
    val vehicleDetailViewModel: VehicleDetailViewModel = viewModel(
        factory = VehicleDetailViewModel.Factory
    )
    val vehicleMileageViewModel: VehicleMileageViewModel = viewModel(
        factory = VehicleMileageViewModel.Factory
    )
    val detailState by vehicleDetailViewModel.uiState.collectAsState()
    val mileageState by vehicleMileageViewModel.uiState.collectAsState()
    VehicleScreenContent(
        vehicle = detailState.vehicle,
        vehicleAttachments = detailState.vehicleAttachments,
        vehicleMileage = mileageState.mileage,
        onMileageClick = onMileageClick,
        onEditVehicle = onEditVehicle,
        onDeleteVehicle = {
            vehicleDetailViewModel.deleteVehicle()
            onBack()
        },
        onBack = onBack,
        onAddMileage = onAddMileage,
        onExportMileage = onExportMileage
    )

}

@Composable
fun VehicleScreenContent(
    vehicle: Vehicle?,
    vehicleAttachments: List<VehicleAttachment>?,
    vehicleMileage: List<Mileage>,
    onMileageClick: (String) -> Unit,
    onEditVehicle: (String) -> Unit,
    onDeleteVehicle: () -> Unit,
    onBack: () -> Unit,
    onAddMileage: (String) -> Unit,
    onExportMileage: (String) -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // TODO: :START: Make composable: ScreenColumn
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
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
                    text = vehicle?.nickname?.let { "\"$it\"" } ?: "",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // TODO: Implement the following as "trashcan" and "pencil" icons
                // Edit
                ClickableIcon(
                    icon = Icons.Filled.Edit,
                    contentDescription = "Edit vehicle",
                    onClick = { onEditVehicle(vehicle?.vehicleID.orEmpty()) }
                )
                // Delete
                ClickableIcon(
                    icon = Icons.Filled.Delete,
                    contentDescription = "Delete vehicle",
                    onClick = { showDeleteConfirmation = true }
                )
            }
        }

//        Row {
            // TODO: Average MPG, Average monthly gallons?, Average monthly miles?
//        }

        // TODO: :START: Make composable: CardSurface
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
//                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(vehicleMileage, key = { it.mileageID }) { mileage ->
                    MileageCard(
                        mileage = mileage, onClick = { onMileageClick(it.mileageID) })
                }

            }
        }
        // TODO: :FINISH: Make composable: CardSurface

        Row() {
//            AddMileageButton(onAddMileage(vehicleID)
//            ExportMileageButton(onExportMileage(vehicleID))
        }

    }
    // TODO: :FINISH: Make composable: ScreenColumn

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete vehicle?") },
            text = { Text("This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    vehicle?.let { onDeleteVehicle() }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
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
    val testVehicleAttachment = VehicleAttachment(
        attachmentID = "the-test-attachment-uuid",
        URI = "/test/attachment/uri",
        vehicle = testVehicle.vehicleID
    )
    val testVehicleAttachmentList = listOf(testVehicleAttachment)

    val testMileage = Mileage(
        mileageID = "the-test-mileage-uuid",
        timestamp = Instant.now().toString(),
        odometerMiles = 80085.69,
        volumeGallons = 6.9,
        isFullTank = true,
        fuelType = FuelType.REGULAR,
        totalDollars = 19.84,
        journal = "This is a test mileage log.",
        vehicle = testVehicle.vehicleID
    )
    val testMileageList = listOf(testMileage)

    val testMileageAttachment = MileageAttachment(
        attachmentID = "the-test-mileage-attachment-uuid",
        URI = "/test/mileage/attachment/uri",
        mileage = testMileage.mileageID
    )

    FuelBuddyTheme {
        VehicleScreenContent(
            vehicle = testVehicle,
            vehicleAttachments = testVehicleAttachmentList,
            vehicleMileage = testMileageList,
            onEditVehicle = {},
            onDeleteVehicle = {},
            onMileageClick = {},
            onExportMileage = {},
            onBack = {},
            onAddMileage = {},
        )
    }
}
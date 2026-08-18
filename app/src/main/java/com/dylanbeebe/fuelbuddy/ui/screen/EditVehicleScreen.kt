package com.dylanbeebe.fuelbuddy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.ui.component.ActionFAB
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.viewmodel.VehicleDetailViewModel

@Composable
fun EditVehicleScreen(
    modifier: Modifier = Modifier, onBack: () -> Unit, onVehicleDeleted: () -> Unit
) {
    /**
     * Vehicle Edit Screen
     *
     * Add new vehicle or update an existing one.
     * */
    val vehicleDetailViewModel: VehicleDetailViewModel = viewModel(
        factory = VehicleDetailViewModel.Factory
    )
    val detailState by vehicleDetailViewModel.uiState.collectAsState()
    EditVehicleScreenContent(
        vehicle = detailState.vehicle,
        onSaveVehicle = { vehicle ->
            if (detailState.vehicle == null) {
                vehicleDetailViewModel.addVehicle(vehicle)
            } else {
                vehicleDetailViewModel.updateVehicle(vehicle)
            }
            onBack()
        },
        onDeleteVehicle = {
            vehicleDetailViewModel.deleteVehicle()
            onVehicleDeleted()
        },
        onBack = onBack
    )

}

@Composable
fun EditVehicleScreenContent(
    vehicle: Vehicle?,
    onSaveVehicle: (Vehicle) -> Unit,
    onDeleteVehicle: () -> Unit,
    onBack: () -> Unit,
) {
    var nickname by remember(vehicle) { mutableStateOf(vehicle?.nickname ?: "") }
    var make by remember(vehicle) { mutableStateOf(vehicle?.make ?: "") }
    var model by remember(vehicle) { mutableStateOf(vehicle?.model ?: "") }
    var modelYear by remember(vehicle) { mutableStateOf(vehicle?.modelYear?.toString() ?: "") }
    var plate by remember(vehicle) { mutableStateOf(vehicle?.plate ?: "") }

    var showDeleteConfirmation by remember { mutableStateOf(false) }

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
                    onClick = { onBack() })
                // Title
                Text(
                    text = if (vehicle == null) "Add vehicle" else "Edit vehicle",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            // Delete (only meaningful for an existing vehicle)
//            if (vehicle != null) {
//                ClickableIcon(
//                    icon = Icons.Filled.Delete,
//                    contentDescription = "Delete vehicle",
//                    onClick = { showDeleteConfirmation = true })
//            }
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
            ) {
                item {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Nickname") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = make,
                        onValueChange = { make = it },
                        label = { Text("Make") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = modelYear,
                        onValueChange = { modelYear = it },
                        label = { Text("Model year") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = plate,
                        onValueChange = { plate = it },
                        label = { Text("License plate") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            if (vehicle != null) {
                // Delete FAB
                FloatingActionButton(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.CenterStart),
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete vehicle",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Save FAB
            FloatingActionButton(
                onClick = {
                    val vehicleToSave = if (vehicle == null) {
                        Vehicle(
                            nickname = nickname,
                            make = make,
                            model = model,
                            modelYear = modelYear.toIntOrNull(),
                            plate = plate,
                        )
                    } else {
                        vehicle.copy(
                            nickname = nickname,
                            make = make,
                            model = model,
                            modelYear = modelYear.toIntOrNull(),
                            plate = plate,
                        )
                    }
                    onSaveVehicle(vehicleToSave)
                },
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterEnd),
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save vehicle",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete vehicle?") },
            text = { Text("This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    onDeleteVehicle()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            })
    }
}
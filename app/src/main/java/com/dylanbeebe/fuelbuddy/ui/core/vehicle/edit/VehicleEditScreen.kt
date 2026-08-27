package com.dylanbeebe.fuelbuddy.ui.core.vehicle.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.form.VehicleForm

@Composable
fun VehicleEditScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onVehicleDeleted: () -> Unit
) {
    val viewModel: VehicleEditViewModel = viewModel(
        factory = VehicleEditViewModel.Factory
    )

    val vehicle by viewModel.vehicleWithAttachments.collectAsState()
    val current = vehicle ?: return

    VehicleForm(
        formTitle = "Edit",
        initialVehicle = current,
        onSave = { input ->
            viewModel.update(current.vehicle.vehicleID, input)
            onBack()
        },
        onDelete = {
            viewModel.delete()
            onVehicleDeleted()
        },
        onBack = onBack
    )
}
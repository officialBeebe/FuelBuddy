package com.dylanbeebe.fuelbuddy.ui.core.vehicle.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.form.VehicleForm


@Composable
fun VehicleAddScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val viewModel: VehicleAddViewModel = viewModel(
        factory = VehicleAddViewModel.Factory
    )
    VehicleForm(
        formTitle = "Add vehicle",
        initialVehicle = null,
        onSave = {input ->
            viewModel.add(input)
            onBack()
        },
        onBack = onBack
    )
}
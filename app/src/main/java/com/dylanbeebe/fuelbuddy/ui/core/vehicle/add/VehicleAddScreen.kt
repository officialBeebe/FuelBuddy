package com.dylanbeebe.fuelbuddy.ui.core.vehicle.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.R
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
        formTitle = stringResource(R.string.vehicle_add_screen_title),
        initialVehicle = null,
        onSave = {input ->
            viewModel.add(input)
            onBack()
        },
        onBack = onBack
    )
}
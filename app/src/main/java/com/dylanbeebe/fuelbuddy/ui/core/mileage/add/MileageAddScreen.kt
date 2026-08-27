package com.dylanbeebe.fuelbuddy.ui.core.mileage.add

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.ui.core.mileage.form.MileageForm
import kotlin.collections.emptyList

@Composable
fun MileageAddScreen(
    onBack: () -> Unit
) {
    val viewModel: MileageAddViewModel = viewModel(
        factory = MileageAddViewModel.Factory
    )
    MileageForm(
        formTitle = "Add",
        initialMileage = null,
        attachments = emptyList(),
        vehicleID = viewModel.vehicleID,
        onSave = { input, attachments, _ ->
            viewModel.addMileage(input, attachments)
            onBack()
        },
        onBack = onBack
    )
}
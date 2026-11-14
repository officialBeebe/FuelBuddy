package com.dylanbeebe.fuelbuddy.ui.core.mileage.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.R
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
        formTitle = stringResource(R.string.mileage_add_screen_title),
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
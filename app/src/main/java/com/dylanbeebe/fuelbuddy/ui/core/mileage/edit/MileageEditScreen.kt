package com.dylanbeebe.fuelbuddy.ui.core.mileage.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.ui.core.mileage.form.MileageForm

@Composable
fun MileageEditScreen(
    onBack: () -> Unit
) {
    val viewModel: MileageEditViewModel = viewModel(
        factory = MileageEditViewModel.Factory
    )

    val mileageWithAttachments by viewModel.mileageWithAttachments.collectAsState()
    // observeMileageWithAttachments emits null until the first value arrives
    // (or if the record is gone); nothing to edit yet in either case.
    val current = mileageWithAttachments ?: return

    MileageForm(
        formTitle = "Edit",
        initialMileage = current.mileage,
        attachments = current.attachments,
        vehicleID = current.mileage.vehicleID,
        onSave = { input, newAttachments, removedAttachments ->
            viewModel.updateMileage(input, newAttachments, removedAttachments)
            onBack()
        },
        onDelete = {
            viewModel.delete()
            onBack()
        },
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun MileageFormPreview() {
    MileageForm(
        formTitle = "Edit",
        initialMileage = null,
        attachments = emptyList(),
        vehicleID = "something-something-something",
        onSave = { _, _, _ -> },
        onDelete = {},
        onBack = {}
    )
}
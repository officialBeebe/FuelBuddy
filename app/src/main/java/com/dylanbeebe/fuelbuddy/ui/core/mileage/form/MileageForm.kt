package com.dylanbeebe.fuelbuddy.ui.core.mileage.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import java.io.File

@Composable
fun MileageForm(
    formTitle: String,
    initialMileage: Mileage?,
    attachments: List<MileageAttachment>,
    vehicleID: String?,
    onSave: (
        MileageInput,
        List<MileageAttachmentInput>,
        List<MileageAttachment> // attachments to delete
    ) -> Unit,
    onDelete: (() -> Unit)? = null,
    onBack: () -> Unit,
) {
    // Form state
    var odometerMiles by remember(initialMileage) {
        mutableStateOf(initialMileage?.odometerMiles?.toString() ?: "")
    }

    var volumeGallons by remember(initialMileage) {
        mutableStateOf(initialMileage?.volumeGallons?.toString() ?: "")
    }

    var totalDollars by remember(initialMileage) {
        mutableStateOf(initialMileage?.totalDollars?.toString() ?: "")
    }

    var fuelType by remember(initialMileage) {
        mutableStateOf(initialMileage?.fuelType ?: FuelType.REGULAR)
    }

    var isFullTank by remember(initialMileage) {
        mutableStateOf(initialMileage?.isFullTank ?: true)
    }

    var journal by remember(initialMileage) {
        mutableStateOf(initialMileage?.journal ?: "")
    }

    // Attachments are always staged here and only flushed to
    // MileageAttachmentInput on save — same path for add and edit.
    var stagedAttachments by remember {
        mutableStateOf<List<File>>(emptyList())
    }

    var removedAttachments by remember(initialMileage) {
        mutableStateOf<List<MileageAttachment>>(emptyList())
    }

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
                ClickableIcon(
                    icon = Icons.Filled.ChevronLeft,
                    contentDescription = "Go back",
                    onClick = { onBack() })
                Text(
                    text = formTitle,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

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
                    item {
                        MileageFormFields(
                            odometerMiles = odometerMiles,
                            onOdometerMilesChange = { odometerMiles = it },

                            volumeGallons = volumeGallons,
                            onVolumeGallonsChange = { volumeGallons = it },

                            totalDollars = totalDollars,
                            onTotalDollarsChange = { totalDollars = it },

                            fuelType = fuelType,
                            onFuelTypeChange = { fuelType = it },

                            isFullTank = isFullTank,
                            onFullTankChange = { isFullTank = it },

                            journal = journal,
                            onJournalChange = { journal = it }
                        )
                    }

                    item {
                        MileageFormAttachments(
                            existingAttachments = attachments.filterNot { existing ->
                                removedAttachments.any { it.attachmentID == existing.attachmentID }
                            },
                            stagedAttachments = stagedAttachments,
                            onStageAttachments = { newFiles ->
                                stagedAttachments = stagedAttachments + newFiles
                            },
                            onRemoveExisting = { attachment ->
                                removedAttachments = removedAttachments + attachment
                            },
                            onRemoveStaged = { file ->
                                stagedAttachments = stagedAttachments - file
                            }
                        )
//                        MileageAttachments(
//                            existingAttachments = attachments,
//                            stagedAttachments = stagedAttachments,
//                            onStageAttachments = { newFiles ->
//                                stagedAttachments = stagedAttachments + newFiles
//                            }
//                        )
                    }
                }
            }

            MileageFormActions(
                canDelete = onDelete != null,
                onDelete = { onDelete?.invoke() },
                onSave = {
                    val odometer = odometerMiles.toDoubleOrNull()
                    val volume = volumeGallons.toDoubleOrNull()
                    val total = totalDollars.toDoubleOrNull()

                    if (odometer == null || volume == null || total == null) return@MileageFormActions
                    if (vehicleID == null) return@MileageFormActions

                    val input = MileageInput(
                        odometerMiles = odometer,
                        volumeGallons = volume,
                        isFullTank = isFullTank,
                        fuelType = fuelType,
                        totalDollars = total,
                        journal = journal.ifBlank { null },
                    )

                    val attachmentInputs = stagedAttachments.map { file ->
                        MileageAttachmentInput(filepath = file.absolutePath)
                    }

                    onSave(input, attachmentInputs, removedAttachments)
                }
            )
        }
    }
}
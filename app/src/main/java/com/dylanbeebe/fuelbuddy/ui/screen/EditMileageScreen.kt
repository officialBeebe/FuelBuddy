package com.dylanbeebe.fuelbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.dylanbeebe.fuelbuddy.data.room.entity.FuelType
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.component.rememberCameraLauncher
import com.dylanbeebe.fuelbuddy.ui.component.rememberMultiPhotoPickerLauncher
import com.dylanbeebe.fuelbuddy.ui.viewmodel.MileageDetailViewModel
import java.io.File

@Composable
fun EditMileageScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onMileageDeleted: () -> Unit
) {
    val mileageDetailViewModel: MileageDetailViewModel = viewModel(
        factory = MileageDetailViewModel.Factory
    )
    val detailState by mileageDetailViewModel.uiState.collectAsState()
    EditMileageScreenContent(
        mileage = detailState.mileage,
        mileageAttachments = detailState.mileageAttachments,
        vehicleID = detailState.vehicleID,
        onAddAttachment = { mileageAttachment ->
            mileageDetailViewModel.addAttachment(mileageAttachment)
        },
        onSaveMileage = { mileage, attachments ->
            if (detailState.mileage == null) {
                mileageDetailViewModel.addMileage(mileage, attachments)
            } else {
                mileageDetailViewModel.updateMileage(mileage)
            }
            onBack()
        },
        onDeleteMileage = {
            mileageDetailViewModel.deleteMileage()
            onMileageDeleted()
        },
        onBack = onBack,
    )
}

/** Unified view of an attachment for display, whether already persisted or staged pending save. */
private sealed interface AttachmentDisplayItem {
    val filePath: String

    data class Existing(val attachment: MileageAttachment) : AttachmentDisplayItem {
        override val filePath: String get() = attachment.filePath
    }

    data class Staged(val file: File) : AttachmentDisplayItem {
        override val filePath: String get() = file.absolutePath
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMileageScreenContent(
    mileage: Mileage?,
    mileageAttachments: List<MileageAttachment>,
    vehicleID: String?,
    onAddAttachment: (MileageAttachment) -> Unit,
    onSaveMileage: (Mileage, List<MileageAttachment>) -> Unit,
    onDeleteMileage: () -> Unit,
    onBack: () -> Unit,
) {
    var odometerMiles by remember(mileage) { mutableStateOf(mileage?.odometerMiles?.toString() ?: "") }
    var volumeGallons by remember(mileage) { mutableStateOf(mileage?.volumeGallons?.toString() ?: "") }
    var totalDollars by remember(mileage) { mutableStateOf(mileage?.totalDollars?.toString() ?: "") }
    var isFullTank by remember(mileage) { mutableStateOf(mileage?.isFullTank ?: true) }
    var fuelType by remember(mileage) { mutableStateOf(mileage?.fuelType ?: FuelType.REGULAR) }
    var journal by remember(mileage) { mutableStateOf(mileage?.journal ?: "") }

    var showFuelTypeMenu by remember { mutableStateOf(false) }
    var stagedAttachments by remember { mutableStateOf<List<File>>(emptyList()) }
    var viewingAttachmentPath by remember { mutableStateOf<String?>(null) }

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
                ClickableIcon(
                    icon = Icons.Filled.ChevronLeft,
                    contentDescription = "Go back",
                    onClick = { onBack() })
                Text(
                    text = if (mileage == null) "Add mileage" else "Edit mileage",
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = odometerMiles,
                        onValueChange = { odometerMiles = it },
                        label = { Text("Odometer (mi)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = volumeGallons,
                        onValueChange = { volumeGallons = it },
                        label = { Text("Volume (gallons)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    OutlinedTextField(
                        value = totalDollars,
                        onValueChange = { totalDollars = it },
                        label = { Text("Total cost ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    ExposedDropdownMenuBox(
                        expanded = showFuelTypeMenu,
                        onExpandedChange = { showFuelTypeMenu = it },
                    ) {
                        OutlinedTextField(
                            value = fuelType.name.lowercase().replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fuel type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showFuelTypeMenu) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                        )
                        ExposedDropdownMenu(
                            expanded = showFuelTypeMenu,
                            onDismissRequest = { showFuelTypeMenu = false },
                        ) {
                            FuelType.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option.name.lowercase()
                                                .replaceFirstChar { it.uppercase() })
                                    },
                                    onClick = {
                                        fuelType = option
                                        showFuelTypeMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isFullTank,
                            onCheckedChange = { isFullTank = it }
                        )
                        Text("Full tank")
                    }
                }
                item {
                    OutlinedTextField(
                        value = journal,
                        onValueChange = { journal = it },
                        label = { Text("Journal") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // Attachments
                item {
                    val displayItems: List<AttachmentDisplayItem> =
                        mileageAttachments.map { AttachmentDisplayItem.Existing(it) } +
                                stagedAttachments.map { AttachmentDisplayItem.Staged(it) }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            displayItems.forEach { displayItem ->
                                SubcomposeAsyncImage(
                                    model = File(displayItem.filePath),
                                    contentDescription = "Attachment",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { viewingAttachmentPath = displayItem.filePath },
                                    loading = {
                                        Box(
                                            Modifier.fillMaxSize()
                                                .background(MaterialTheme.colorScheme.surfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Filled.Image,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(60.dp)
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            Modifier.fillMaxSize()
                                                .background(MaterialTheme.colorScheme.errorContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Filled.BrokenImage,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.size(60.dp)
                                            )
                                        }
                                    }
                                )
                            }

                            // Take photo
                            val takePhoto = rememberCameraLauncher(subdir = "mileage_attachments") { file ->
                                if (mileage != null) {
                                    onAddAttachment(MileageAttachment(filePath = file.absolutePath, mileage = mileage.mileageID))
                                } else {
                                    stagedAttachments = stagedAttachments + file
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable(onClickLabel = "Take photo") { takePhoto() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PhotoCamera,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            // Photo picker
                            val pickPhotos =
                                rememberMultiPhotoPickerLauncher(subdir = "mileage_attachments") { files ->
                                    if (mileage != null) {
                                        files.forEach { file ->
                                            onAddAttachment(
                                                MileageAttachment(
                                                    filePath = file.absolutePath,
                                                    mileage = mileage.mileageID
                                                )
                                            )
                                        }
                                    } else {
                                        stagedAttachments = stagedAttachments + files
                                    }
                                }
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable(onClickLabel = "Pick photo") { pickPhotos() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PhotoLibrary,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            if (mileage != null) {
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
                        contentDescription = "Delete mileage",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Save FAB
            FloatingActionButton(
                onClick = {
                    val odometer = odometerMiles.toDoubleOrNull()
                    val volume = volumeGallons.toDoubleOrNull()
                    val total = totalDollars.toDoubleOrNull()
                    if (odometer != null && volume != null && total != null && vehicleID != null) {
                        val mileageToSave = if (mileage == null) {
                            Mileage(
                                odometerMiles = odometer,
                                volumeGallons = volume,
                                isFullTank = isFullTank,
                                fuelType = fuelType,
                                totalDollars = total,
                                journal = journal.ifBlank { null },
                                vehicle = vehicleID,
                            )
                        } else {
                            mileage.copy(
                                odometerMiles = odometer,
                                volumeGallons = volume,
                                isFullTank = isFullTank,
                                fuelType = fuelType,
                                totalDollars = total,
                                journal = journal.ifBlank { null },
                            )
                        }
                        val attachmentsToSave = stagedAttachments.map { file ->
                            MileageAttachment(
                                filePath = file.absolutePath,
                                mileage = mileageToSave.mileageID
                            )
                        }
                        onSaveMileage(mileageToSave, attachmentsToSave)
                    }
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
                    contentDescription = "Save mileage",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete mileage entry?") },
            text = { Text("This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    mileage?.let { onDeleteMileage() }
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

    viewingAttachmentPath?.let { path ->
        Dialog(onDismissRequest = { viewingAttachmentPath = null }) {
            AsyncImage(
                model = File(path),
                contentDescription = "Attachment preview",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewingAttachmentPath = null }
            )
        }
    }
}
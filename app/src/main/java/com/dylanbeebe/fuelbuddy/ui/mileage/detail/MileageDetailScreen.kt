package com.dylanbeebe.fuelbuddy.ui.mileage.detail

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MileageScreen(
    onEditMileage: (String) -> Unit, onBack: () -> Unit
) {
    /**
     * Mileage Screen
     *
     * Mileage details and attachments.
     *
     * Can launch mileage edit screen.
     */
    val mileageDetailViewModel: MileageDetailViewModel = viewModel(
        factory = MileageDetailViewModel.Factory
    )
    val detailState by mileageDetailViewModel.uiState.collectAsState()
    MileageScreenContent(
        mileage = detailState.mileage,
        mileageAttachments = detailState.mileageAttachments,
        onEditMileage = onEditMileage,
        onDeleteMileage = {
            mileageDetailViewModel.deleteMileage()
            onBack()
        },
        onBack = onBack
    )
}

@Composable
fun MileageScreenContent(
    mileage: Mileage?,
    mileageAttachments: List<MileageAttachment>,
    onEditMileage: (String) -> Unit,
    onDeleteMileage: () -> Unit,
    onBack: () -> Unit,
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var viewingAttachmentPath by remember { mutableStateOf<String?>(null) }

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
                val dateText = mileage?.timestamp?.let {
                    Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        .format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                } ?: ""
                Text(
                    text = dateText, style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }


        // Interface
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
                if (mileage != null) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Details section
                            // Heading
                            Text(
                                text = "Details",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                            // Odometer
                            Text(
                                text = "Odometer: %.1f mi".format(mileage.odometerMiles),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            // Gallons
                            Text(
                                text = "Volume: %.2f gallons".format(mileage.volumeGallons),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            // Total cost
                            Text(
                                text = "Total cost: $%.2f".format(mileage.totalDollars),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            // Price per gallon
                            Text(
                                text = "Price per gallon: $%.2f/gallon".format(mileage.totalDollars / mileage.volumeGallons),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            // Fill status
                            Text(
                                text = "Fill status: " + if (mileage.isFullTank) "Full tank" else "Partial fill",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            // Fuel type
                            Text(
                                text = "Fuel type: ${
                                    mileage.fuelType.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                }", style = MaterialTheme.typography.bodyLarge)

                            // Journal section
                            mileage.journal?.let { journal ->
                                HorizontalDivider()
                                // Heading
                                Text(
                                    text = "Journal",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                                // Journal entry
                                Text(
                                    text = journal,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Attachments section
                            if (mileageAttachments.isNotEmpty()) {
                                HorizontalDivider()
                                // Heading
                                Text(
                                    text = "Attachments",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                                // Attachments
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp, Alignment.CenterHorizontally
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    mileageAttachments.forEach { attachment ->
                                        SubcomposeAsyncImage(
                                            model = File(attachment.filePath),
                                            contentDescription = "Attachment",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .clickable {
                                                    viewingAttachmentPath = attachment.filePath
                                                },
                                            loading = {
                                                Box(
                                                    Modifier
                                                        .fillMaxSize()
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
                                                    Modifier
                                                        .fillMaxSize()
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
                                            })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
                onClick = { onEditMileage(mileage?.mileageID.orEmpty()) },
                modifier = Modifier.size(64.dp).align(Alignment.End),
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit mileage",
                    modifier = Modifier.size(32.dp)
                )
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
                    .clickable { viewingAttachmentPath = null })
        }
    }
}
package com.dylanbeebe.fuelbuddy.ui.core.mileage.detail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.dylanbeebe.fuelbuddy.R
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import java.io.File
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun MileageDetailScreen(
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
        mileageAttachmentEntities = detailState.mileageAttachments,
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
    mileageAttachmentEntities: List<MileageAttachment>,
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
                    contentDescription = stringResource(R.string.common_go_back),
                    onClick = { onBack() })
                // Title
                val dateText = mileage?.timestamp?.let {
                    Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                }.orEmpty()
                Text(
                    text = dateText, style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Edit Mileage Icon
//            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ClickableIcon(
                icon = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.common_edit),
                onClick = { onEditMileage(mileage?.mileageID.orEmpty()) },
                modifier = Modifier.size(24.dp)
            )
//            }
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
                            // Details Section Heading
                            Text(
                                text = stringResource(R.string.mileage_details_header),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )

                            // Odometer
                            Text(
                                text = stringResource(
                                    R.string.mileage_details_odometer,
                                    mileage.odometerMiles
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            // Volume
                            Text(
                                text = stringResource(
                                    R.string.mileage_details_volume,
                                    mileage.volumeGallons
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            // Total cost
                            val costText = remember(mileage.totalDollars) {
                                NumberFormat.getCurrencyInstance().format(mileage.totalDollars)
                            }
                            Text(
                                text = stringResource(
                                    R.string.mileage_details_total_cost,
                                    costText
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            // TODO: Rename mileage (and vehicle) model fields to unit-agnostic names
                            // (odometerMiles -> odometer, volumeGallons -> volume, totalDollars -> totalCost)
                            // and add unit/currency settings.

                            // Price per gallon
                            val pricePerGallon = if (mileage.volumeGallons > 0) {
                                mileage.totalDollars / mileage.volumeGallons
                            } else null
                            val pricePerGallonText = remember(pricePerGallon) {
                                NumberFormat.getCurrencyInstance().apply {
                                    minimumFractionDigits = 3
                                    maximumFractionDigits = 3
                                }.format(pricePerGallon)
                            }
                            Text(
                                text = stringResource(
                                    R.string.mileage_details_price_per_gallon,
                                    pricePerGallonText
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            // Fill status
                            Text(
                                text = stringResource(
                                    if (mileage.isFullTank) R.string.mileage_details_fill_status_full
                                    else R.string.mileage_details_fill_status_partial
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            // Fuel type
                            Text(
                                text = "Fuel type: ${
                                    mileage.fuelType.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                }", style = MaterialTheme.typography.bodyLarge)


                            // Journal Section
                            mileage.journal?.let { journal ->
                                HorizontalDivider()
                                // Heading
                                Text(
                                    text = stringResource(R.string.mileage_details_journal_header),
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


                            // Attachments Section
                            if (mileageAttachmentEntities.isNotEmpty()) {
                                HorizontalDivider()
                                // Heading
                                Text(
                                    text = stringResource(R.string.mileage_details_attachments_header),
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
                                    mileageAttachmentEntities.forEach { attachment ->
                                        SubcomposeAsyncImage(
                                            model = File(attachment.filepath),
                                            contentDescription = stringResource(R.string.mileage_attachment),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .clickable {
                                                    viewingAttachmentPath = attachment.filepath
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
//        FloatingActionButton(
//                onClick = { onEditMileage(mileage?.mileageID.orEmpty()) },
//                modifier = Modifier.size(64.dp).align(Alignment.End),
//                shape = RoundedCornerShape(16.dp),
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary,
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Edit mileage",
//                    modifier = Modifier.size(32.dp)
//                )
//            }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.delete_mileage_alert_title)) },
            text = { Text(stringResource(R.string.delete_mileage_alert_text)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    mileage?.let { onDeleteMileage() }
                }) {
                    Text(stringResource(R.string.common_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(stringResource(R.string.common_cancel))
                }
            })
    }

    viewingAttachmentPath?.let { path ->
        Dialog(onDismissRequest = { viewingAttachmentPath = null }) {
            AsyncImage(
                model = File(path),
                contentDescription = stringResource(R.string.mileage_attachment_preview),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewingAttachmentPath = null })
        }
    }
}
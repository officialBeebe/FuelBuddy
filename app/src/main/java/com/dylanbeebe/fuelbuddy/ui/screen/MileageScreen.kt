package com.dylanbeebe.fuelbuddy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.ui.viewmodel.MileageDetailViewModel
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MileageScreen(
    onEditMileage: (String) -> Unit,
    onHome: () -> Unit
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
        onHome = onHome
    )
}

@Composable
fun MileageScreenContent(
    mileage: Mileage?,
    mileageAttachments: List<MileageAttachment>,
    onEditMileage: (String) -> Unit,
    onHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            val dateText = mileage?.timestamp?.let {
                Instant.parse(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
            } ?: ""
            Text(text = dateText)
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (mileage != null) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            HorizontalDivider()
                            Text(
                                text = "Odometer: %.1f mi".format(mileage.odometerMiles),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "Gallons: %.2f gal".format(mileage.volumeGallons),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "Fuel type: ${
                                    mileage.fuelType.name.lowercase().replaceFirstChar { it.uppercase() }
                                }",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (mileage.latitude != null && mileage.longitude != null) {
                                Text(
                                    text = "Location: %.4f, %.4f".format(mileage.latitude, mileage.longitude),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            mileage.journal?.let { journal ->
                                Text(
                                    text = journal,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (mileageAttachments.isNotEmpty()) {
                    item {
                        HorizontalDivider()
                        Text(
                            text = "Attachments",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    items(mileageAttachments, key = { it.attachmentID }) { attachment ->
                        AsyncImage(
                            model = File(attachment.URI),
                            contentDescription = null,
                            placeholder = rememberVectorPainter(Icons.Filled.Image),
                            error = rememberVectorPainter(Icons.Filled.BrokenImage),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
        }

        Row {
            // HomeButton(onHome)
            // EditMileageButton(onEditMileage(mileage?.mileageID))
        }
    }
}
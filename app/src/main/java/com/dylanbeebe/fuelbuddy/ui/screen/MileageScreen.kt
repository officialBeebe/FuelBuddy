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
import androidx.compose.ui.text.font.FontWeight
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
    onEditMileage: (String) -> Unit, onHome: () -> Unit
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
                horizontalAlignment = Alignment.CenterHorizontally,
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

                            // Location section
                            if (mileage.latitude != null && mileage.longitude != null) {
                                HorizontalDivider()
                                // Heading
                                Text(
                                    text = "Location",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                                // Coordinates
                                Text(
                                    text = "Latitude: %.4f".format(
                                        mileage.latitude
                                    ), style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Longitude: %.4f".format(
                                        mileage.longitude
                                    ), style = MaterialTheme.typography.bodyLarge
                                )
                            }

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
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    mileageAttachments.forEach { attachment ->
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
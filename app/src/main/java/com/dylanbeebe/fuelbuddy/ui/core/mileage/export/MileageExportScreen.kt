package com.dylanbeebe.fuelbuddy.ui.core.mileage.export

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.R
import com.dylanbeebe.fuelbuddy.domain.model.mileage.ExportMileageInput
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.detail.VehicleDetailViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun MileageExportScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    val vehicleDetailViewModel: VehicleDetailViewModel = viewModel(
        factory = VehicleDetailViewModel.Factory
    )
    val mileageExportViewModel: MileageExportViewModel = viewModel(
        factory = MileageExportViewModel.Factory
    )

    val uiState by mileageExportViewModel.uiState.collectAsState()

    val savedMessage = stringResource(R.string.mileage_export_download_success)
    val failedMessage = stringResource(R.string.mileage_export_failed)
    val shareIntentTitle = stringResource(R.string.mileage_export_share_intent_title)
    ExportMileageScreenContent(
        onDownload = { rangeMode, start, end, format ->
            mileageExportViewModel.downloadMileage(
                vehicleID = uiState.vehicleID,
                input = ExportMileageInput(rangeMode, start, end, format),
                context = context,
                onSuccess = {
                    Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
                    onBack()
                },
                onError = {
                    Toast.makeText(context, it ?: failedMessage, Toast.LENGTH_SHORT).show()
                },
            )
        },
        onShare = { rangeMode, start, end, format ->
            mileageExportViewModel.exportMileage(
                vehicleID = uiState.vehicleID,
                input = ExportMileageInput(rangeMode, start, end, format),
                context = context,
                onSuccess = { intent ->
                    context.startActivity(Intent.createChooser(intent, shareIntentTitle))
                    onBack()
                },
                onError = {
                    Toast.makeText(context, it ?: failedMessage, Toast.LENGTH_SHORT).show()
                },
            )
        },
        onCancel = onBack,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportMileageScreenContent(
    onDownload: (ExportRangeMode, LocalDate?, LocalDate?, ExportFormat) -> Unit,
    onShare: (ExportRangeMode, LocalDate?, LocalDate?, ExportFormat) -> Unit,
    onCancel: () -> Unit,
    onBack: () -> Unit,
) {
    var rangeMode by remember { mutableStateOf(ExportRangeMode.LATEST) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var format by remember { mutableStateOf(ExportFormat.CSV) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            ClickableIcon(
                icon = Icons.Filled.ChevronLeft,
                contentDescription = stringResource(R.string.common_go_back),
                onClick = { onBack() })
            Text(
                text = stringResource(R.string.mileage_export_screen_title),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        // TODO: Move FABs to bottom of shaded surface area with form fields.
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Range", style = MaterialTheme.typography.titleMedium)
                            RadioOption(
                                label = stringResource(R.string.mileage_export_range_latest),
                                selected = rangeMode == ExportRangeMode.LATEST,
                                onSelect = {
                                    rangeMode =
                                        ExportRangeMode.LATEST
                                }
                            )
                            RadioOption(
                                label = stringResource(R.string.mileage_export_range_custom),
                                selected = rangeMode == ExportRangeMode.CUSTOM,
                                onSelect = {
                                    rangeMode =
                                        ExportRangeMode.CUSTOM
                                }
                            )
                        }
                    }

                    if (rangeMode == ExportRangeMode.CUSTOM) {
                        item {
                            DatePickerField(
                                label = stringResource(R.string.mileage_export_start_date),
                                date = startDate,
                                onDatePicked = { startDate = it }
                            )
                        }
                        item {
                            DatePickerField(
                                label = stringResource(R.string.mileage_export_end_date),
                                date = endDate,
                                onDatePicked = { endDate = it }
                            )
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.mileage_export_format), style = MaterialTheme.typography.titleMedium)
                            RadioOption(
                                label = "CSV",
                                selected = format == ExportFormat.CSV,
                                onSelect = {
                                    format =
                                        ExportFormat.CSV
                                }
                            )
                            RadioOption(
                                label = "JSON",
                                selected = format == ExportFormat.JSON,
                                onSelect = {
                                    format =
                                        ExportFormat.JSON
                                }
                            )
                        }
                    }
                }

                ExportActionBar(
                    exportEnabled = rangeMode == ExportRangeMode.LATEST ||
                            (startDate != null && endDate != null),
                    onCancel = onCancel,
                    onDownload = { onDownload(rangeMode, startDate, endDate, format) },
                    onShare = { onShare(rangeMode, startDate, endDate, format) },
                )
            }
        }
    }
}

@Composable
private fun ExportActionBar(
    exportEnabled: Boolean,
    onCancel: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ExportFab(
            icon = Icons.Default.Close,
            contentDescription = stringResource(R.string.common_cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onClick = onCancel,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ExportFab(
                icon = Icons.Default.Download,
                contentDescription = stringResource(R.string.mileage_export_download),
                containerColor = if (exportEnabled) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.surface,
                contentColor = if (exportEnabled) MaterialTheme.colorScheme.onSecondary
                else MaterialTheme.colorScheme.onSurface,
                onClick = { if (exportEnabled) onDownload() },
            )
            ExportFab(
                icon = Icons.Default.Share,
                contentDescription = stringResource(R.string.mileage_export_share),
                containerColor = if (exportEnabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface,
                contentColor = if (exportEnabled) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface,
                onClick = { if (exportEnabled) onShare() },
            )
        }
    }
}

@Composable
private fun ExportActionBar(
    exportEnabled: Boolean,
    onCancel: () -> Unit,
    onExport: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ExportFab(
            icon = Icons.Default.Close,
            contentDescription = stringResource(R.string.common_cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onClick = onCancel,
        )
        ExportFab(
            icon = Icons.Default.Send,
            contentDescription = stringResource(R.string.common_export),
            containerColor = if (exportEnabled) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface,
            contentColor = if (exportEnabled) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            onClick = { if (exportEnabled) onExport() },
        )
    }
}

@Composable
private fun ExportFab(
    icon: ImageVector,
    contentDescription: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Icon(icon, contentDescription, Modifier.size(32.dp))
    }
}

@Composable
private fun RadioOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(label: String, date: LocalDate?, onDatePicked: (LocalDate) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = date?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { showDialog = true }
        )


        if (showDialog) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = date?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
                    ?.toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val picked =
                                Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                            onDatePicked(picked)
                        }
                        showDialog = false
                    }) { Text(stringResource(R.string.common_ok)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.common_close)) }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
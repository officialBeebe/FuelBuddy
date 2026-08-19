package com.dylanbeebe.fuelbuddy.ui.mileage.export

import android.content.Intent
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
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon
import com.dylanbeebe.fuelbuddy.ui.vehicle.VehicleDetailViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ExportMileageScreen(
    onBack: () -> Unit,
) {
    val vehicleDetailViewModel: VehicleDetailViewModel = viewModel(
        factory = VehicleDetailViewModel.Factory
    )
    val vehicleMileageViewModel: VehicleMileageViewModel = viewModel(
        factory = VehicleMileageViewModel.Factory
    )
    val detailState by vehicleDetailViewModel.uiState.collectAsState()
    val context = LocalContext.current

    detailState.vehicle?.let { vehicle ->
        ExportMileageScreenContent(
            onExport = { rangeMode, start, end, format ->
                vehicleMileageViewModel.exportMileage(
                    vehicle = vehicle,
                    rangeMode = rangeMode,
                    startDate = start,
                    endDate = end,
                    format = format,
                ) { intent ->
                    context.startActivity(Intent.createChooser(intent, "Share mileage export"))
                    onBack()
                }
            },
            onCancel = onBack,
            onBack = onBack,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportMileageScreenContent(
    onExport: (ExportRangeMode, LocalDate?, LocalDate?, ExportFormat) -> Unit,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            ClickableIcon(
                icon = Icons.Filled.ChevronLeft,
                contentDescription = "Go back",
                onClick = { onBack() })
            Text(
                text = "Export mileage",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
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
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Range", style = MaterialTheme.typography.titleMedium)
                        RadioOption(
                            label = "Latest (pending, not yet exported)",
                            selected = rangeMode == ExportRangeMode.LATEST,
                            onSelect = { rangeMode = ExportRangeMode.LATEST }
                        )
                        RadioOption(
                            label = "Custom date range",
                            selected = rangeMode == ExportRangeMode.CUSTOM,
                            onSelect = { rangeMode = ExportRangeMode.CUSTOM }
                        )
                    }
                }

                if (rangeMode == ExportRangeMode.CUSTOM) {
                    item {
                        DatePickerField(
                            label = "Start date",
                            date = startDate,
                            onDatePicked = { startDate = it }
                        )
                    }
                    item {
                        DatePickerField(
                            label = "End date",
                            date = endDate,
                            onDatePicked = { endDate = it }
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Format", style = MaterialTheme.typography.titleMedium)
                        RadioOption(
                            label = "CSV",
                            selected = format == ExportFormat.CSV,
                            onSelect = { format = ExportFormat.CSV }
                        )
                        RadioOption(
                            label = "JSON",
                            selected = format == ExportFormat.JSON,
                            onSelect = { format = ExportFormat.JSON }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            // Cancel FAB
            FloatingActionButton(
                onClick = onCancel,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterStart),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel export",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Export FAB
            val exportEnabled =
                rangeMode == ExportRangeMode.LATEST || (startDate != null && endDate != null)
            FloatingActionButton(
                onClick = { if (exportEnabled) onExport(rangeMode, startDate, endDate, format) },
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterEnd),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                containerColor = if (exportEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (exportEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Export",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
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
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
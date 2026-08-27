package com.dylanbeebe.fuelbuddy.ui.core.mileage.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MileageFormFields(
    odometerMiles: String,
    onOdometerMilesChange: (String) -> Unit,
    volumeGallons: String,
    onVolumeGallonsChange: (String) -> Unit,
    totalDollars: String,
    onTotalDollarsChange: (String) -> Unit,
    fuelType: FuelType,
    onFuelTypeChange: (FuelType) -> Unit,
    isFullTank: Boolean,
    onFullTankChange: (Boolean) -> Unit,
    journal: String,
    onJournalChange: (String) -> Unit,
) {
    var showFuelTypeMenu by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Odometer
        OutlinedTextField(
            value = odometerMiles,
            onValueChange = onOdometerMilesChange,
            label = { Text("Odometer (mi)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Gallons
        OutlinedTextField(
            value = volumeGallons,
            onValueChange = onVolumeGallonsChange,
            label = { Text("Volume (gallons)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )

        // Total dollars
        OutlinedTextField(
            value = totalDollars,
            onValueChange = onTotalDollarsChange,
            label = { Text("Total cost ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )

        // Fuel type dropdown
        // Fuel type dropdown
        val fuelTypeLabel = fuelType.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }

        ExposedDropdownMenuBox(
            expanded = showFuelTypeMenu,
            onExpandedChange = { showFuelTypeMenu = it },
        ) {
            OutlinedTextField(
                value = fuelTypeLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fuel type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = showFuelTypeMenu
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
            )

            ExposedDropdownMenu(
                expanded = showFuelTypeMenu,
                onDismissRequest = { showFuelTypeMenu = false },
            ) {
                FuelType.entries.forEach { option ->
                    val optionLabel = option.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }

                    DropdownMenuItem(
                        text = { Text(optionLabel) },
                        onClick = {
                            onFuelTypeChange(option)
                            showFuelTypeMenu = false
                        }
                    )
                }
            }
        }

        // Fuel tank?
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isFullTank,
                onCheckedChange = onFullTankChange
            )
            Text("Full tank")
        }

        // Journal
        OutlinedTextField(
            value = journal,
            onValueChange = onJournalChange,
            label = { Text("Journal") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
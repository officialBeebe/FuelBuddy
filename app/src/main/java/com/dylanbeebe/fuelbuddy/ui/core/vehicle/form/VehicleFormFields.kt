package com.dylanbeebe.fuelbuddy.ui.core.vehicle.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.R

@Composable
fun VehicleFormFields(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    make: String,
    onMakeChange: (String) -> Unit,
    model: String,
    onModelChange: (String) -> Unit,
    modelYear: String,
    onModelYearChange: (String) -> Unit,
    plate: String,
    onPlateChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        // Nickname
        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text(stringResource(R.string.vehicle_form_nickname)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Make
        OutlinedTextField(
            value = make,
            onValueChange = onMakeChange,
            label = { Text(stringResource(R.string.vehicle_form_make)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Model
        OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            label = { Text(stringResource(R.string.vehicle_form_model)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Model year
        OutlinedTextField(
            value = modelYear,
            onValueChange = onModelYearChange,
            label = { Text(stringResource(R.string.vehicle_form_model_year)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Plate
        OutlinedTextField(
            value = plate,
            onValueChange = onPlateChange,
            label = { Text(stringResource(R.string.vehicle_form_license_plate)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}
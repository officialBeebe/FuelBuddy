package com.dylanbeebe.fuelbuddy.ui.core.vehicle.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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
            label = { Text("Nickname") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Make
        OutlinedTextField(
            value = make,
            onValueChange = onMakeChange,
            label = { Text("Make") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Model
        OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            label = { Text("Model") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Model year
        OutlinedTextField(
            value = modelYear,
            onValueChange = onModelYearChange,
            label = { Text("Model Year") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Plate
        OutlinedTextField(
            value = plate,
            onValueChange = onPlateChange,
            label = { Text("Plate") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}
package com.dylanbeebe.fuelbuddy.ui.core.vehicle.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.ui.component.ClickableIcon

@Composable
fun VehicleForm(
    formTitle: String,
    initialVehicle: VehicleWithAttachments?, // multipurpose form for new and existing vehicle edits
    onSave: (VehicleInput) -> Unit,
    onDelete: (() -> Unit)? = null, // delete only available on VehicleEditScreen
    onBack: () -> Unit
) {
    // Form state
    var nickname by remember(initialVehicle) {
        mutableStateOf(initialVehicle?.vehicle?.nickname ?: "")
    }
    var make by remember(initialVehicle) {
        mutableStateOf(initialVehicle?.vehicle?.make ?: "")
    }
    var model by remember(initialVehicle) {
        mutableStateOf(initialVehicle?.vehicle?.model ?: "")
    }
    var modelYear by remember(initialVehicle) {
        mutableStateOf(initialVehicle?.vehicle?.modelYear?.toString() ?: "")
    }
    var plate by remember(initialVehicle) {
        mutableStateOf(initialVehicle?.vehicle?.plate ?: "")
    }

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
                    text = formTitle, style = MaterialTheme.typography.headlineLarge.copy(
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
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
                        VehicleFormFields(
                            nickname = nickname,
                            onNicknameChange = { nickname = it },
                            make = make,
                            onMakeChange = { make = it },
                            model = model,
                            onModelChange = { model = it },
                            modelYear = modelYear,
                            onModelYearChange = { modelYear = it },
                            plate = plate,
                            onPlateChange = { plate = it }
                        )
                    }

                }

            }

            VehicleFormActions(
                canDelete = onDelete != null,
                onDelete = { onDelete?.invoke() },
                onSave = {
                    val modelYear = modelYear.toIntOrNull()

                    val input = VehicleInput(
                        nickname = nickname,
                        make = make,
                        model = model,
                        modelYear = modelYear,
                        plate = plate
                    )

                    onSave(input)
                }
            )
        }
    }
}
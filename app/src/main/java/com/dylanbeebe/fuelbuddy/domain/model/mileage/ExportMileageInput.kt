package com.dylanbeebe.fuelbuddy.domain.model.mileage

import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode
import java.time.LocalDate

data class ExportMileageInput(
    val rangeMode: com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val format: com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat,
)
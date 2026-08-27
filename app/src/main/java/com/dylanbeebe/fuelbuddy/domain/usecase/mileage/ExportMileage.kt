package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.dylanbeebe.fuelbuddy.domain.model.mileage.ExportMileageInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode
import java.io.File
import java.time.LocalDate

/**
 * Prepares mileage export as CSV or JSON (depends on input),
 * sets selected mileage records to "exported",
 * and returns a file sharing intent to the caller.
 * */

class ExportMileage(
    private val mileageRepository: MileageRepository,
    private val vehicleRepository: VehicleRepository,
) {
    suspend fun invoke(vehicleID: String, input: ExportMileageInput, context: Context): Intent {
        val vehicle = vehicleRepository.getVehicle(vehicleID)
        val toExport = fetchMileage(vehicleID, input)
        check(toExport.isNotEmpty()) { "No mileage entries to export" }

        val content = buildContent(toExport, input.format)
        val file = writeExportFile(context, vehicle, input.format, content)
        val intent = buildShareIntent(context, file, input.format)

        toExport.forEach { m -> mileageRepository.update(m.copy(isExported = true)) }

        return intent
    }

    private suspend fun fetchMileage(vehicleID: String, input: ExportMileageInput): List<Mileage> =
        when (input.rangeMode) {
            _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode.LATEST -> mileageRepository.getUnexportedMileageForVehicle(vehicleID)
            _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode.CUSTOM -> {
                val start = requireNotNull(input.startDate) { "startDate required for custom range" }
                val end = requireNotNull(input.endDate) { "endDate required for custom range" }
                mileageRepository.getMileageForVehicleInRange(
                    vehicleID,
                    start.atStartOfDay().toString(),
                    end.atTime(23, 59, 59).toString(),
                )
            }
        }

    private fun buildContent(mileage: List<Mileage>, format: com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat): String =
        when (format) {
            _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat.CSV -> mileageRepository.buildMileageExportCsv(mileage)
            _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat.JSON -> mileageRepository.buildMileageExportJson(mileage)
        }

    private fun writeExportFile(context: Context, vehicle: Vehicle, format: com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat, content: String): File {
        val extension = if (format == _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat.CSV) "csv" else "json"
        val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
        val safeName = vehicle.nickname.trim().replace(Regex("\\s+"), "-")
        return File(exportDir, "${safeName}_${LocalDate.now()}.$extension").apply { writeText(content) }
    }

    private fun buildShareIntent(context: Context, file: File, format: com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat): Intent {
        val mimeType = if (format == _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat.CSV) "text/csv" else "application/json"
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        return Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
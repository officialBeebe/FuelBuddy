package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import android.content.Context
import android.content.Intent
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.dylanbeebe.fuelbuddy.R
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
        val toExport = fetchMileage(vehicleID, input, context)
        check(toExport.isNotEmpty()) { context.getString(R.string.mileage_export_no_entries) }

        val content = buildContent(toExport, input.format)
        val file = writeExportFile(context, vehicle, input.format, content)
        val intent = buildShareIntent(context, file, input.format)

        toExport.forEach { m -> mileageRepository.update(m.copy(isExported = true)) }

        return intent
    }

    private suspend fun fetchMileage(vehicleID: String, input: ExportMileageInput, context: Context): List<Mileage> =
        when (input.rangeMode) {
            ExportRangeMode.LATEST -> mileageRepository.getUnexportedMileageForVehicle(vehicleID)
            ExportRangeMode.CUSTOM -> {
                val start = requireNotNull(input.startDate) { context.getString(R.string.mileage_export_start_date_required) }
                val end = requireNotNull(input.endDate) { context.getString(R.string.mileage_export_end_date_required) }
                mileageRepository.getMileageForVehicleInRange(
                    vehicleID,
                    start.atStartOfDay().toString(),
                    end.atTime(23, 59, 59).toString(),
                )
            }
        }

    private fun buildContent(mileage: List<Mileage>, format: ExportFormat): String =
        when (format) {
            ExportFormat.CSV -> mileageRepository.buildMileageExportCsv(mileage)
            ExportFormat.JSON -> mileageRepository.buildMileageExportJson(mileage)
        }

    private fun writeExportFile(context: Context, vehicle: Vehicle, format: ExportFormat, content: String): File {
        val extension = if (format == ExportFormat.CSV) "csv" else "json"
        val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
        val safeName = vehicle.nickname.trim().replace(Regex("\\s+"), "-")
        return File(exportDir, "${safeName}_${LocalDate.now()}.$extension").apply { writeText(content) }
    }

    private fun buildShareIntent(context: Context, file: File, format: ExportFormat): Intent {
        val mimeType = if (format == ExportFormat.CSV) "text/csv" else "application/json"
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        return Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
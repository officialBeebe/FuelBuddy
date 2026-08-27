package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.dylanbeebe.fuelbuddy.domain.model.mileage.ExportMileageInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportFormat
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.ExportRangeMode
import java.io.IOException
import java.time.LocalDate

/**
 * Prepares mileage export as CSV or JSON (depends on input),
 * writes it directly to the device's Downloads folder via MediaStore,
 * and marks the exported records as "exported".
 * */
class DownloadMileage(
    private val mileageRepository: MileageRepository,
    private val vehicleRepository: VehicleRepository,
) {
    suspend fun invoke(vehicleID: String, input: ExportMileageInput, context: Context): Uri {
        val vehicle = vehicleRepository.getVehicle(vehicleID)
        val toExport = fetchMileage(vehicleID, input)
        check(toExport.isNotEmpty()) { "No mileage entries to export" }
        val content = buildContent(toExport, input.format)
        val uri = writeToDownloads(context, vehicle.nickname, input.format, content)
        toExport.forEach { m -> mileageRepository.update(m.copy(isExported = true)) }
        return uri
    }

    private suspend fun fetchMileage(vehicleID: String, input: ExportMileageInput): List<Mileage> =
        when (input.rangeMode) {
            ExportRangeMode.LATEST -> mileageRepository.getUnexportedMileageForVehicle(vehicleID)
            ExportRangeMode.CUSTOM -> {
                val start = requireNotNull(input.startDate) { "startDate required for custom range" }
                val end = requireNotNull(input.endDate) { "endDate required for custom range" }
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

    private fun writeToDownloads(context: Context, vehicleNickname: String, format: ExportFormat, content: String): Uri {
        val extension = if (format == ExportFormat.CSV) "csv" else "json"
        val mimeType = if (format == ExportFormat.CSV) "text/csv" else "application/json"
        val safeName = vehicleNickname.trim().replace(Regex("\\s+"), "-")
        val fileName = "FuelBuddyExport_${safeName}_${LocalDate.now()}.$extension"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("MediaStore insert failed for $fileName")

        resolver.openOutputStream(uri)?.use { out ->
            out.write(content.toByteArray())
        } ?: throw IOException("Could not open output stream for $fileName")

        return uri
    }
}
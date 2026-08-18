package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.dao.MileageAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MileageRepository(
    private val mileageDAO: MileageDAO,
    private val mileageAttachmentDAO: MileageAttachmentDAO
) {
    fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>> =
        mileageDAO.observeAllForVehicle(vehicleID)

    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?> =
        mileageDAO.observeMileageWithAttachments(mileageID)

    suspend fun getUnexportedForVehicle(vehicleID: String): List<Mileage> =
        mileageDAO.getUnexportedForVehicle(vehicleID)

    suspend fun getForVehicleInRange(vehicleID: String, start: String, end: String): List<Mileage> =
        mileageDAO.getForVehicleInRange(vehicleID, start, end)

    suspend fun insert(mileage: Mileage) = mileageDAO.insert(mileage)
    suspend fun update(mileage: Mileage) = mileageDAO.update(mileage)
    suspend fun delete(mileage: Mileage) = mileageDAO.delete(mileage)

    suspend fun addAttachment(attachment: MileageAttachment) = mileageAttachmentDAO.insert(attachment)
    suspend fun removeAttachment(attachment: MileageAttachment) {
        mileageAttachmentDAO.delete(attachment)
        File(attachment.filePath).delete()
    }

    fun buildExportCsv(mileage: List<Mileage>): String = buildString {
        appendLine("timestamp,odometerMiles,volumeGallons,isFullTank,fuelType,totalDollars,journal")
        mileage.forEach { m ->
            appendLine(
                listOf(
                    m.timestamp, m.odometerMiles, m.volumeGallons, m.isFullTank,
                    m.fuelType.name, m.totalDollars, csvEscape(m.journal ?: ""),
                ).joinToString(",")
            )
        }
    }

    fun buildExportJson(mileage: List<Mileage>): String =
        Json { prettyPrint = true }.encodeToString(mileage)

    private fun csvEscape(value: String): String {
        val needsQuoting = value.contains(",") || value.contains("\"") || value.contains("\n")
        return if (needsQuoting) "\"${value.replace("\"", "\"\"")}\"" else value
    }
}
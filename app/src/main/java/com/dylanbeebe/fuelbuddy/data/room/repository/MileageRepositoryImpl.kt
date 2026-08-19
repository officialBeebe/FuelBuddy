package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.dao.MileageAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MileageRepositoryImpl (
    private val mileageDAO: MileageDAO,
    private val mileageAttachmentDAO: MileageAttachmentDAO
) : MileageRepository {
    override fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>> =
        mileageDAO.observeAllForVehicle(vehicleID)

    override fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?> =
        mileageDAO.observeMileageWithAttachments(mileageID)

    override suspend fun getUnexportedForVehicle(vehicleID: String): List<Mileage> =
        mileageDAO.getUnexportedForVehicle(vehicleID)

    override suspend fun getForVehicleInRange(vehicleID: String, start: String, end: String): List<Mileage> =
        mileageDAO.getForVehicleInRange(vehicleID, start, end)

    override suspend fun insert(mileage: Mileage) = mileageDAO.insert(mileage)
    override suspend fun update(mileage: Mileage) = mileageDAO.update(mileage)
    override suspend fun delete(mileage: Mileage) = mileageDAO.delete(mileage)

    override suspend fun addAttachment(attachment: MileageAttachment) = mileageAttachmentDAO.insert(attachment)
    override suspend fun removeAttachment(attachment: MileageAttachment) {
        mileageAttachmentDAO.delete(attachment)
        File(attachment.filePath).delete()
    }

    override fun buildExportCsv(mileage: List<Mileage>): String = buildString {
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

    override fun buildExportJson(mileage: List<Mileage>): String =
        Json { prettyPrint = true }.encodeToString(mileage)

    private fun csvEscape(value: String): String {
        val needsQuoting = value.contains(",") || value.contains("\"") || value.contains("\n")
        return if (needsQuoting) "\"${value.replace("\"", "\"\"")}\"" else value
    }
}
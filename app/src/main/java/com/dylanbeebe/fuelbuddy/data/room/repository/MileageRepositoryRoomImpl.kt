package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.room.dao.MileageAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.mapper.mileage.toDomain
import com.dylanbeebe.fuelbuddy.data.room.mapper.mileage.toEntity
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MileageRepositoryRoomImpl(
    private val mileageDAO: MileageDAO,
    private val mileageAttachmentDAO: MileageAttachmentDAO
) : MileageRepository {
    override suspend fun getMileage(mileageID: String): Mileage {
        return mileageDAO.getMileage(mileageID).toDomain()
    }

    override fun observeAllMileageForVehicle(vehicleID: String): Flow<List<Mileage>> =
        mileageDAO.observeAllForVehicle(vehicleID).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeMileageWithAttachments(
        mileageID: String
    ): Flow<MileageWithAttachments?> =
        mileageDAO.observeMileageWithAttachments(mileageID)
            .map { entity ->
                entity?.toDomain()
            }

    override suspend fun getUnexportedMileageForVehicle(
        vehicleID: String
    ): List<Mileage> =
        mileageDAO
            .getUnexportedForVehicle(vehicleID)
            .map { it.toDomain() }

    override suspend fun getMileageForVehicleInRange(
        vehicleID: String,
        start: String,
        end: String
    ): List<Mileage> =
        mileageDAO
            .getForVehicleInRange(vehicleID, start, end)
            .map { it.toDomain() }

    override suspend fun insert(mileage: Mileage) = mileageDAO.insert(mileage.toEntity())
    override suspend fun update(mileage: Mileage) = mileageDAO.update(mileage.toEntity())
    override suspend fun delete(mileage: Mileage) = mileageDAO.delete(mileage.toEntity())

    override suspend fun addAttachment(attachment: MileageAttachment) =
        mileageAttachmentDAO.insert(attachment.toEntity())

    override suspend fun deleteAttachment(attachment: MileageAttachment) {
        mileageAttachmentDAO.delete(attachment.toEntity())
        File(attachment.filepath).delete()
    }

    override fun buildMileageExportCsv(mileage: List<Mileage>): String = buildString {
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

    override fun buildMileageExportJson(mileage: List<Mileage>): String =
        Json { prettyPrint = true }.encodeToString(mileage)

    private fun csvEscape(value: String): String {
        val needsQuoting = value.contains(",") || value.contains("\"") || value.contains("\n")
        return if (needsQuoting) "\"${value.replace("\"", "\"\"")}\"" else value
    }
}
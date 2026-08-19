package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import kotlinx.coroutines.flow.Flow

interface MileageRepository {
    fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>>
    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?>
    suspend fun getUnexportedForVehicle(vehicleID: String): List<Mileage>
    suspend fun getForVehicleInRange(vehicleID: String, start: String, end: String): List<Mileage>
    suspend fun insert(mileage: Mileage)
    suspend fun update(mileage: Mileage)
    suspend fun delete(mileage: Mileage)
    suspend fun addAttachment(attachment: MileageAttachment)
    suspend fun removeAttachment(attachment: MileageAttachment)
    fun buildExportCsv(mileage: List<Mileage>): String
    fun buildExportJson(mileage: List<Mileage>): String
}
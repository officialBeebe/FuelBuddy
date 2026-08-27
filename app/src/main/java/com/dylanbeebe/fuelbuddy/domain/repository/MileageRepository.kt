package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments
import kotlinx.coroutines.flow.Flow

interface MileageRepository {
    suspend fun getMileage(mileageID: String): Mileage
    fun observeAllMileageForVehicle(vehicleID: String): Flow<List<Mileage>>
    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?>
    suspend fun getUnexportedMileageForVehicle(vehicleID: String): List<Mileage>
    suspend fun getMileageForVehicleInRange(vehicleID: String, start: String, end: String): List<Mileage>
    suspend fun insert(mileage: Mileage)
    suspend fun update(mileage: Mileage)
    suspend fun delete(mileage: Mileage)
    suspend fun addAttachment(attachment: MileageAttachment)
    suspend fun deleteAttachment(attachment: MileageAttachment)
    fun buildMileageExportCsv(mileage: List<Mileage>): String
    fun buildMileageExportJson(mileage: List<Mileage>): String
}
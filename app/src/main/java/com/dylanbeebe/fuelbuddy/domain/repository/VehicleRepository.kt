package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun observeAllVehicles(): Flow<List<Vehicle>>
    fun observeVehicleWithAttachments(vehicleId: String): Flow<VehicleWithAttachments?>
    suspend fun insert(vehicle: Vehicle)
    suspend fun update(vehicle: Vehicle)
    suspend fun delete(vehicle: Vehicle)
    suspend fun addAttachment(attachment: VehicleAttachment)
    suspend fun removeAttachment(attachment: VehicleAttachment)
}
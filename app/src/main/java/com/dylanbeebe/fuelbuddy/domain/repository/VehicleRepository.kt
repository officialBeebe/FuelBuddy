package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleAttachment
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    suspend fun getVehicle(vehicleID: String): Vehicle
    fun observeAllVehicles(): Flow<List<Vehicle>>
    fun observeVehicle(vehicleID: String): Flow<Vehicle>
    fun observeVehicleWithAttachments(vehicleId: String): Flow<VehicleWithAttachments?>
    suspend fun insert(vehicle: Vehicle)
    suspend fun update(vehicle: Vehicle)
    suspend fun delete(vehicle: Vehicle)
    suspend fun addAttachment(attachment: VehicleAttachment)
    suspend fun deleteAttachment(attachment: VehicleAttachment)
}
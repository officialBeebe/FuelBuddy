package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.dao.VehicleAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import kotlinx.coroutines.flow.Flow
import java.io.File

class VehicleRepository(
    private val vehicleDAO: VehicleDAO,
    private val vehicleAttachmentDAO: VehicleAttachmentDAO
) {
    fun observeAllVehicles(): Flow<List<Vehicle>> = vehicleDAO.observeAllVehicles()

    fun observeVehicleWithAttachments(vehicleId: String): Flow<VehicleWithAttachments?> =
        vehicleDAO.observeVehicleWithAttachments(vehicleId)

    suspend fun insert(vehicle: Vehicle) = vehicleDAO.insert(vehicle)
    suspend fun update(vehicle: Vehicle) = vehicleDAO.update(vehicle)
    suspend fun delete(vehicle: Vehicle) = vehicleDAO.delete(vehicle)

    suspend fun addAttachment(attachment: VehicleAttachment) =
        vehicleAttachmentDAO.insert(attachment)

    suspend fun removeAttachment(attachment: VehicleAttachment) {
        vehicleAttachmentDAO.delete(attachment)
        File(attachment.filePath).delete()
    }
}
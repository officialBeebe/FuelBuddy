package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.dao.VehicleAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class VehicleRepositoryImpl(
    private val vehicleDAO: VehicleDAO,
    private val vehicleAttachmentDAO: VehicleAttachmentDAO
) : VehicleRepository {

    override fun observeAllVehicles(): Flow<List<Vehicle>> = vehicleDAO.observeAllVehicles()

    override fun observeVehicleWithAttachments(vehicleId: String): Flow<VehicleWithAttachments?> =
        vehicleDAO.observeVehicleWithAttachments(vehicleId)

    override suspend fun insert(vehicle: Vehicle) = vehicleDAO.insert(vehicle)

    override suspend fun update(vehicle: Vehicle) = vehicleDAO.update(vehicle)

    override suspend fun delete(vehicle: Vehicle) = vehicleDAO.delete(vehicle)

    override suspend fun addAttachment(attachment: VehicleAttachment) =
        vehicleAttachmentDAO.insert(attachment)

    override suspend fun deleteAttachment(attachment: VehicleAttachment) {
        vehicleAttachmentDAO.delete(attachment)
        File(attachment.filePath).delete()
    }
}
package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.room.dao.VehicleAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.mapper.vehicle.toDomain
import com.dylanbeebe.fuelbuddy.data.room.mapper.vehicle.toEntity
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleAttachment
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class VehicleRepositoryRoomImpl(
    private val vehicleDAO: VehicleDAO,
    private val vehicleAttachmentDAO: VehicleAttachmentDAO
) : VehicleRepository {
    override suspend fun getVehicle(vehicleID: String): Vehicle =
        vehicleDAO.getVehicle(vehicleID).toDomain()

    override fun observeAllVehicles(): Flow<List<Vehicle>> =
        vehicleDAO.observeAllVehicles().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeVehicle(vehicleID: String): Flow<Vehicle> =
        vehicleDAO.observeVehicle(vehicleID).map { entity ->
            entity.toDomain()
        }

    override fun observeVehicleWithAttachments(
        vehicleId: String
    ): Flow<VehicleWithAttachments?> =
        vehicleDAO.observeVehicleWithAttachments(vehicleId).map { entity ->
            entity?.toDomain()
        }

    override suspend fun insert(vehicle: Vehicle) = vehicleDAO.insert(vehicle.toEntity())

    override suspend fun update(vehicle: Vehicle) = vehicleDAO.update(vehicle.toEntity())

    override suspend fun delete(vehicle: Vehicle) = vehicleDAO.delete(vehicle.toEntity())

    override suspend fun addAttachment(attachment: VehicleAttachment) =
        vehicleAttachmentDAO.insert(attachment.toEntity())

    override suspend fun deleteAttachment(attachment: VehicleAttachment) {
        vehicleAttachmentDAO.delete(attachment.toEntity())
        File(attachment.filepath).delete()
    }
}
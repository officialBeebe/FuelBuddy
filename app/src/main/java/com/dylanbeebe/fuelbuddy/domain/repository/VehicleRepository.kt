package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments

class VehicleRepository(private val vehicleDAO: VehicleDAO) {
    suspend fun allVehicles(): List<Vehicle> = vehicleDAO.getAll()

    suspend fun getVehicleWithAttachments(vehicleId: String): VehicleWithAttachments =
        vehicleDAO.getVehicleWithAttachments(vehicleId)

    suspend fun insert(vehicle: Vehicle) {
        vehicleDAO.insert(vehicle)
    }
}
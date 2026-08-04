package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val vehicleDAO: VehicleDAO) {
    suspend fun allVehicles(): List<Vehicle> = vehicleDAO.getAll()

    suspend fun insert(vehicle: Vehicle) {
        vehicleDAO.insert(vehicle)
    }
}
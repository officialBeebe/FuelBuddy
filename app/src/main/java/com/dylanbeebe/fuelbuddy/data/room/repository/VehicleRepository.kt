package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val vehicleDAO: VehicleDAO) {
    val allVehicles: Flow<List<Vehicle>> = vehicleDAO.getAll()

    suspend fun insert(vehicle: Vehicle) {
        vehicleDAO.insert(vehicle)
    }
}
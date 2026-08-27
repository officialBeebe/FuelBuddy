package com.dylanbeebe.fuelbuddy.domain.usecase.vehicle

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository

class DeleteVehicle(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(
        vehicleID: String
    ) {
        val vehicle: Vehicle = repository.getVehicle(vehicleID)
        repository.delete(vehicle)
    }
}
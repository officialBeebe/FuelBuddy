package com.dylanbeebe.fuelbuddy.domain.usecase.vehicle

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import java.util.UUID

class UpdateVehicle(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(
        vehicleID: String,
        input: VehicleInput
    ) {
        val _vehicle = requireNotNull(repository.getVehicle(vehicleID))
        val vehicle = Vehicle(
            vehicleID = _vehicle.vehicleID,
            nickname = input.nickname,
            make = input.make,
            model = input.model,
            modelYear = input.modelYear,
            plate = input.plate
        )
        repository.update(vehicle)
    }
}
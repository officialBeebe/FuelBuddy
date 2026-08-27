package com.dylanbeebe.fuelbuddy.domain.usecase.vehicle

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import java.util.UUID

class AddVehicle(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(
        input: VehicleInput
    ) {
        val vehicle = Vehicle(
            vehicleID = UUID.randomUUID().toString(),
            nickname = input.nickname,
            make = input.make,
            model = input.model,
            modelYear = input.modelYear,
            plate = input.plate
        )
        repository.insert(vehicle)
    }
}
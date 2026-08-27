package com.dylanbeebe.fuelbuddy.domain.usecase.vehicle

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllVehicles(
    private val repository: VehicleRepository,
) {
    operator fun invoke(): Flow<List<Vehicle>> =
        repository.observeAllVehicles()
}
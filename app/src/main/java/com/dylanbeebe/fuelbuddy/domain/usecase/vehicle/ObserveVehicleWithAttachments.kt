package com.dylanbeebe.fuelbuddy.domain.usecase.vehicle

import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow

class ObserveVehicleWithAttachments(
    private val repository: VehicleRepository
) {
    operator fun invoke(vehicleID: String): Flow<VehicleWithAttachments?> =
        repository.observeVehicleWithAttachments(vehicleID)
}
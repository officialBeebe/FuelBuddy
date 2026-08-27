package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllMileageForVehicle(
    private val repository: MileageRepository
) {
    operator fun invoke(vehicleID: String): Flow<List<Mileage>> =
        repository.observeAllMileageForVehicle(vehicleID)
}
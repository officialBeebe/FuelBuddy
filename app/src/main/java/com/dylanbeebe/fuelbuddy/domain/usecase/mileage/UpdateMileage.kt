package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository

class UpdateMileage(
    private val repository: MileageRepository
) {
    suspend operator fun invoke(
        mileageID: String,
        input: MileageInput
    ) {
        val _mileage = requireNotNull(repository.getMileage(mileageID))
        val mileage = Mileage(
            mileageID = mileageID,
            timestamp = _mileage.timestamp,
            odometerMiles = input.odometerMiles,
            volumeGallons = input.volumeGallons,
            isFullTank = input.isFullTank,
            fuelType = input.fuelType,
            totalDollars = input.totalDollars,
            journal = input.journal,
            isExported = _mileage.isExported,
            vehicleID = _mileage.vehicleID
        )
        repository.update(mileage)
    }
}
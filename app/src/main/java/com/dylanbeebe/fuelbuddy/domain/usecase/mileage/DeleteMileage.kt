package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository

class DeleteMileage(
    private val repository: MileageRepository
) {
    suspend operator fun invoke(
        mileageID: String
        ) {
        val mileage: Mileage = repository.getMileage(mileageID)
        repository.delete(mileage)
    }
}
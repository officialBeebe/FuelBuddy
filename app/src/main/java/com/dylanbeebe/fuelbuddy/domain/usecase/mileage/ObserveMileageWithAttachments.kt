package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import kotlinx.coroutines.flow.Flow

class ObserveMileageWithAttachments(
    private val repository: MileageRepository
) {
    operator fun invoke(mileageID: String): Flow<MileageWithAttachments?> =
        repository.observeMileageWithAttachments(mileageID)
}
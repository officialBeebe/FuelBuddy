package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachmentEntity
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository

class DeleteAttachment(
    private val repository: MileageRepository
) {
    suspend operator fun invoke(
        input: MileageAttachment
    ) {
        repository.deleteAttachment(input)
    }
}
package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import java.util.UUID

class AddAttachment(
    private val repository: MileageRepository
) {
    suspend operator fun invoke(
        mileageID: String,
        input: MileageAttachmentInput
    ) {
        val attachment = MileageAttachment(
            attachmentID = UUID.randomUUID().toString(),
            filepath = input.filepath,
            mileageID = mileageID

        )
        repository.addAttachment(attachment)
    }
}
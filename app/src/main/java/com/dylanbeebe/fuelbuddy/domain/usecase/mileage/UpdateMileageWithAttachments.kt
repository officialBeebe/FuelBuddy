package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import java.util.UUID

class UpdateMileageWithAttachments(
    private val repository: MileageRepository
) {
    suspend operator fun invoke(
        mileageID: String,
        input: MileageInput,
        newAttachments: List<MileageAttachmentInput>,
        removedAttachments: List<MileageAttachment>,
    ) {
        val existing = repository.getMileage(mileageID)
        val mileage = Mileage(
            mileageID = existing.mileageID,
            timestamp = existing.timestamp,
            odometerMiles = input.odometerMiles,
            volumeGallons = input.volumeGallons,
            isFullTank = input.isFullTank,
            fuelType = input.fuelType,
            totalDollars = input.totalDollars,
            journal = input.journal,
            isExported = existing.isExported,
            vehicleID = existing.vehicleID
        )
        repository.update(mileage)

        removedAttachments.forEach { attachment ->
            repository.deleteAttachment(attachment)
        }

        newAttachments.forEach { attachment ->
            repository.addAttachment(
                MileageAttachment(
                    attachmentID = UUID.randomUUID().toString(),
                    filepath = attachment.filepath,
                    mileageID = mileageID
                )
            )
        }
    }
}
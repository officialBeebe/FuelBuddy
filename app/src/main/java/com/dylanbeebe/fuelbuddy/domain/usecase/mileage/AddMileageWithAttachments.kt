package com.dylanbeebe.fuelbuddy.domain.usecase.mileage

import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AddMileageWithAttachments(
    private val repository: MileageRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(
        vehicleID: String,
        input: MileageInput,
        attachments: List<MileageAttachmentInput>
    ) {
        val mileageID = UUID.randomUUID().toString()

        val mileage = Mileage(
            mileageID = mileageID,
            timestamp = Clock.System.now().toString(),
            odometerMiles = input.odometerMiles,
            volumeGallons = input.volumeGallons,
            isFullTank = input.isFullTank,
            fuelType = input.fuelType,
            totalDollars = input.totalDollars,
            journal = input.journal,
            isExported = false,
            vehicleID = vehicleID
        )

        repository.insert(mileage)

        attachments.forEach { attachment ->
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
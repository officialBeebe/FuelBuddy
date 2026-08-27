package com.dylanbeebe.fuelbuddy.data.room.mapper.mileage

import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachmentEntity
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment

fun MileageAttachmentEntity.toDomain(): MileageAttachment =
    MileageAttachment(
        attachmentID = attachmentID,
        filepath = filepath,
        mileageID = mileageID

    )

fun MileageAttachment.toEntity(): MileageAttachmentEntity =
    MileageAttachmentEntity(
        attachmentID = attachmentID,
        filepath = filepath,
        mileageID = mileageID
    )
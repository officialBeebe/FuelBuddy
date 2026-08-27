package com.dylanbeebe.fuelbuddy.data.room.mapper.mileage

import com.dylanbeebe.fuelbuddy.data.room.relation.MileageEntityWithAttachments
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments

fun MileageEntityWithAttachments.toDomain(): MileageWithAttachments =
    MileageWithAttachments(
        mileage = mileage.toDomain(),
        attachments = attachments.map { it.toDomain() },
    )
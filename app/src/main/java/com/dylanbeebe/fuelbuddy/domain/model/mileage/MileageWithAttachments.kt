package com.dylanbeebe.fuelbuddy.domain.model.mileage

data class MileageWithAttachments(
    val mileage: Mileage,
    val attachments: List<MileageAttachment>
)
package com.dylanbeebe.fuelbuddy.domain.model.vehicle

data class VehicleWithAttachments(
    val vehicle: Vehicle,
    val attachments: List<VehicleAttachment>
)
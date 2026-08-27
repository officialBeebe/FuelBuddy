package com.dylanbeebe.fuelbuddy.data.room.mapper.vehicle

import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleEntityWithAttachments
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments

fun VehicleEntityWithAttachments.toDomain(): VehicleWithAttachments =
    VehicleWithAttachments(
        vehicle = vehicle.toDomain(),
        attachments = attachments.map { it.toDomain() },
    )

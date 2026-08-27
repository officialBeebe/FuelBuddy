package com.dylanbeebe.fuelbuddy.data.room.mapper.vehicle

import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachmentEntity
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleAttachment

fun VehicleAttachmentEntity.toDomain(): VehicleAttachment =
    VehicleAttachment(
        attachmentID = attachmentID,
        filepath = filepath,
        vehicleID = vehicleID

    )

fun VehicleAttachment.toEntity(): VehicleAttachmentEntity =
    VehicleAttachmentEntity(
        attachmentID = attachmentID,
        filepath = filepath,
        vehicleID = vehicleID
    )
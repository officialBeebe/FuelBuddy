package com.dylanbeebe.fuelbuddy.data.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleEntity
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachmentEntity

data class VehicleEntityWithAttachments(
    @Embedded val vehicle: VehicleEntity,
    @Relation(
        parentColumn = "vehicleID",
        entityColumn = "vehicleID"
    )
    val attachments: List<VehicleAttachmentEntity>
)
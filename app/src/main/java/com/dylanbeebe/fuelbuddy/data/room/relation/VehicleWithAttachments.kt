package com.dylanbeebe.fuelbuddy.data.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment

data class VehicleWithAttachments(
    @Embedded val vehicle: Vehicle,
    @Relation(
        parentColumn = "vehicleID",
        entityColumn = "vehicle"
    )
    val attachments: List<VehicleAttachment>
)
package com.dylanbeebe.fuelbuddy.data.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment

data class MileageWithAttachments(
    @Embedded val mileage: Mileage,
    @Relation(
        parentColumn = "mileageID",
        entityColumn = "mileage"
    )
    val attachments: List<MileageAttachment>
)
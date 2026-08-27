package com.dylanbeebe.fuelbuddy.data.room.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachmentEntity
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageEntity

data class MileageEntityWithAttachments(
    @Embedded val mileage: MileageEntity,

    @Relation(
        parentColumn = "mileageID",
        entityColumn = "mileageID"
    )
    val attachments: List<MileageAttachmentEntity>
)
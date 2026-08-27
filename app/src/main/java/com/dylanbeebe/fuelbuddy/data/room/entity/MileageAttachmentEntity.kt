package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = MileageEntity::class,
        parentColumns = arrayOf("mileageID"),
        childColumns = arrayOf("mileageID"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class MileageAttachmentEntity(
    @PrimaryKey val attachmentID: String = UUID.randomUUID().toString(),
    val filepath: String,
    val mileageID: String
)

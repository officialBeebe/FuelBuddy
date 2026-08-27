package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = VehicleEntity::class,
        parentColumns = arrayOf("vehicleID"),
        childColumns = arrayOf("vehicleID"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class VehicleAttachmentEntity(
    @PrimaryKey val attachmentID: String = UUID.randomUUID().toString(),
    val filepath: String,
    val vehicleID: String
)

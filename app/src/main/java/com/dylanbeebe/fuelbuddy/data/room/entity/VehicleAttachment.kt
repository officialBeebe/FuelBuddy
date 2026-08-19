package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = Vehicle::class,
        parentColumns = arrayOf("vehicleID"),
        childColumns = arrayOf("vehicle"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class VehicleAttachment(
    @PrimaryKey val attachmentID: String = UUID.randomUUID().toString(),
    val filePath: String,
    val vehicle: String
)

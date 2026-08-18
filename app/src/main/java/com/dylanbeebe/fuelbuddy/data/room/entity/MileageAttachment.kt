package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = Mileage::class,
        parentColumns = arrayOf("mileageID"),
        childColumns = arrayOf("mileage"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class MileageAttachment(
    @PrimaryKey val attachmentID: String = UUID.randomUUID().toString(),
    val filePath: String,
    val mileage: String
)

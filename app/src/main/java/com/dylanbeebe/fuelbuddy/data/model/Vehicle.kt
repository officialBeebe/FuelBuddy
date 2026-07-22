package com.dylanbeebe.fuelbuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

// Foreign Keys: https://stackoverflow.com/a/56707343
@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("userID"),
        childColumns = arrayOf("user"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Vehicle(
    @PrimaryKey val vehicleID: String = UUID.randomUUID().toString(),
    val nickname: String,
    val make: String?,
    val model: String?,
    val modelYear: Int?,
    val plate: String?,
    val user: String,
)




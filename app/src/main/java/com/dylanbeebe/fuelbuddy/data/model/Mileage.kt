package com.dylanbeebe.fuelbuddy.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    foreignKeys = [ForeignKey(
        entity = Vehicle::class,
        parentColumns = arrayOf("vehicleID"),
        childColumns = arrayOf("vehicle"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["timestamp", "vehicle"],
        unique = true
    )]
)
@TypeConverters(FuelTypeConverter::class)
data class Mileage(
    @PrimaryKey val mileageID: String = UUID.randomUUID().toString(),
    val timestamp: String = LocalDateTime.now().toString(),
    val latitude: Double?,
    val longitude: Double?,
    val odometerMiles: Double,
    val volumeGallons: Double,
    val isFullTank: Boolean,
    val fuelType: FuelType,
    val totalDollars: Double,
    val journal: String?,
    val isExported: Boolean = false,
    val vehicle: String,
)
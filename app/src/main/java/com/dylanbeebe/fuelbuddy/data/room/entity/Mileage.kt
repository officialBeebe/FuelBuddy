package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
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
    val timestamp: String = Instant.now().toString(),
    val odometerMiles: Double,
    val volumeGallons: Double,
    val isFullTank: Boolean,
    val fuelType: FuelType,
    val totalDollars: Double,
    val journal: String? = null,
    val isExported: Boolean = false,
    val vehicle: String,
)
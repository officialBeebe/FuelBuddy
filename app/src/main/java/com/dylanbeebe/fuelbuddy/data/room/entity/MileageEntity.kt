package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.annotation.NonNull
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
    tableName = "mileage",
    foreignKeys = [ForeignKey(
        entity = VehicleEntity::class,
        parentColumns = arrayOf("vehicleID"),
        childColumns = arrayOf("vehicleID"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["timestamp", "vehicleID"],
        unique = true
    )]
)
@TypeConverters(FuelTypeConverter::class)
data class MileageEntity(
    @PrimaryKey val mileageID: String = UUID.randomUUID().toString(),
    val timestamp: String = Instant.now().toString(),
    val odometerMiles: Double,
    val volumeGallons: Double,
    val isFullTank: Boolean,
    val fuelType: FuelType,
    val totalDollars: Double,
    val journal: String? = null,
    val isExported: Boolean = false,
    val vehicleID: String,
)
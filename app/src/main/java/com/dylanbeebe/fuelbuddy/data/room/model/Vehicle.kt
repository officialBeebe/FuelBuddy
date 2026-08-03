package com.dylanbeebe.fuelbuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Foreign Keys: https://stackoverflow.com/a/56707343
@Entity
data class Vehicle(
    @PrimaryKey val vehicleID: String = UUID.randomUUID().toString(),
    val nickname: String,
    val make: String?,
    val model: String?,
    val modelYear: Int?,
    val plate: String?,
//    val user: String,
)




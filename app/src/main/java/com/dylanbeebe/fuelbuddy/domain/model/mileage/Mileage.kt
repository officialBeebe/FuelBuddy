package com.dylanbeebe.fuelbuddy.domain.model.mileage

import kotlinx.serialization.Serializable

@Serializable
data class Mileage(
    val mileageID: String,
    val timestamp: String,
    val odometerMiles: Double,
    val volumeGallons: Double,
    val isFullTank: Boolean,
    val fuelType: FuelType,
    val totalDollars: Double,
    val journal: String?,
    val isExported: Boolean,
    val vehicleID: String
)
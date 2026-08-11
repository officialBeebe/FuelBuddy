package com.dylanbeebe.fuelbuddy.data.room.dao

data class MinimalMileage(
    val mileageID: String,
    val timestamp: String,
    val odometerMiles: Double,
    val volumeGallons: Double,
    val totalDollars: Double,
    val isExported: Boolean
)
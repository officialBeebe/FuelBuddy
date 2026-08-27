package com.dylanbeebe.fuelbuddy.domain.model.mileage

data class MileageInput (
        val odometerMiles: Double,
        val volumeGallons: Double,
        val isFullTank: Boolean,
        val fuelType: FuelType,
        val totalDollars: Double,
        val journal: String?,
    )

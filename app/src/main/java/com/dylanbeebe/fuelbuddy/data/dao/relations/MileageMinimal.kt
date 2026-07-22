package com.dylanbeebe.fuelbuddy.data.dao.relations

// TODO: What is the minimal `mileage` record we need to fetch for the UI?
data class MileageMinimal(
    val mileageID: String,
    val timestamp: String,
    val totalDollars: Double,
    val vehicle: String
)

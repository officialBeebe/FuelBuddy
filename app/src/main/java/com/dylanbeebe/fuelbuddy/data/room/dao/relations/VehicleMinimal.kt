package com.dylanbeebe.fuelbuddy.data.dao.relations

// TODO: What is the minimal `vehicle` record we need to fetch for the UI?
data class VehicleMinimal(
    val vehicleID: String,
    val nickname: String,
    val user: String
)
package com.dylanbeebe.fuelbuddy.domain.model.vehicle

data class Vehicle(
    val vehicleID: String,
    val nickname: String,
    val make: String?,
    val model: String?,
    val modelYear: Int?,
    val plate: String?
)
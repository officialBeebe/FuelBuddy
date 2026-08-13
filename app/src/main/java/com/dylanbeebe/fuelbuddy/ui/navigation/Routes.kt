package com.dylanbeebe.fuelbuddy.ui.navigation

import kotlinx.serialization.Serializable

// Displays vehicle inventory.
@Serializable
object Home

// Displays vehicle mileage logs.
@Serializable
data class Vehicle(val vehicleID: String)

// Vehicle details for new and existing vehicles.
@Serializable
data class EditVehicle(val vehicleID: String? = null)

// Mileage details for new and existing mileage logs.
@Serializable
data class EditMileage(val vehicleID: String, val mileageID: String? = null)

// Mileage export form.
@Serializable
data class ExportMileage(val vehicleID: String)
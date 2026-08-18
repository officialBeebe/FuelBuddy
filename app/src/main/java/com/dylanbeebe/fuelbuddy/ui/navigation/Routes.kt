package com.dylanbeebe.fuelbuddy.ui.navigation

import kotlinx.serialization.Serializable

// Displays vehicle inventory.
@Serializable
object Home

// Displays vehicle mileage logs.
@Serializable
data class VehicleDetail(val vehicleID: String)

// Displays a single mileage entry's details and attachments.
@Serializable
data class MileageDetail(val mileageID: String)

// Vehicle details for new and existing vehicles.
@Serializable
data class EditVehicle(val vehicleID: String? = null)

// Mileage details for new and existing mileage logs.
@Serializable
data class EditMileage(val vehicleID: String? = null, val mileageID: String? = null)

// Mileage export form.
@Serializable
data class ExportMileage(val vehicleID: String)
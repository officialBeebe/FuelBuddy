package com.dylanbeebe.fuelbuddy.ui.navigation

import kotlinx.serialization.Serializable

// Displays vehicle inventory.
@Serializable
object VehicleList

@Serializable
object VehicleAdd

@Serializable
data class VehicleDetail(val vehicleID: String)

@Serializable
data class VehicleEdit(val vehicleID: String? = null)

@Serializable
data class MileageAdd(val vehicleID: String)

@Serializable
data class MileageDetail(val mileageID: String)

@Serializable
data class MileageEdit(val mileageID: String)


@Serializable
data class ExportMileage(val vehicleID: String)
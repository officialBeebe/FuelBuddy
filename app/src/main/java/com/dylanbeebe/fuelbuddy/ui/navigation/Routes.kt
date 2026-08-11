package com.dylanbeebe.fuelbuddy.ui.navigation

import kotlinx.serialization.Serializable

// Displays vehicle inventory.
@Serializable
object Home

// Displays vehicle mileage logs.
@Serializable
object Vehicle

// Vehicle details for new and existing vehicles.
@Serializable
object EditVehicle

// Mileage details for new and existing mileage logs.
@Serializable
object EditMileage

// Mileage export form.
@Serializable
object ExportMileage


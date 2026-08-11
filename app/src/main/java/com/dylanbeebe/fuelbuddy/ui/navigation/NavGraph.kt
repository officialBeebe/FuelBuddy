package com.dylanbeebe.fuelbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dylanbeebe.fuelbuddy.ui.screen.EditMileageScreen
import com.dylanbeebe.fuelbuddy.ui.screen.EditVehicleScreen
import com.dylanbeebe.fuelbuddy.ui.screen.ExportMileageScreen
import com.dylanbeebe.fuelbuddy.ui.screen.HomeScreen
import com.dylanbeebe.fuelbuddy.ui.screen.VehicleScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> { HomeScreen(
            onVehicleClick = { vehicleID: String -> navController.navigate(Vehicle)},
            onAddVehicle = { navController.navigate(EditVehicle)}
        ) }
        composable<Vehicle> { VehicleScreen() }
        composable<EditVehicle> { EditVehicleScreen() }
        composable<EditMileage> { EditMileageScreen() }
        composable<ExportMileage> { ExportMileageScreen() }
    }
}
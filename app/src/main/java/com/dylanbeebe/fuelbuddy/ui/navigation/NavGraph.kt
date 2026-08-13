package com.dylanbeebe.fuelbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dylanbeebe.fuelbuddy.ui.screen.EditMileageScreen
import com.dylanbeebe.fuelbuddy.ui.screen.EditVehicleScreen
import com.dylanbeebe.fuelbuddy.ui.screen.ExportMileageScreen
import com.dylanbeebe.fuelbuddy.ui.screen.HomeScreen
import com.dylanbeebe.fuelbuddy.ui.screen.MileageScreen
import com.dylanbeebe.fuelbuddy.ui.screen.VehicleScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                onVehicleClick = { vehicleID: String -> navController.navigate(Vehicle(vehicleID)) },
                onAddVehicle = { navController.navigate(EditVehicle()) }
            )
        }
        composable<Vehicle> {
            VehicleScreen(
                onMileageClick = { mileageID: String ->
                    navController.navigate(
                        MileageDetail(
                            mileageID
                        )
                    )
                },
                onEditVehicle = { vehicleID: String -> navController.navigate(EditVehicle(vehicleID)) },
                onHome = { navController.navigate(Home) },
                onAddMileage = { vehicleID: String -> navController.navigate(EditMileage(vehicleID = vehicleID)) },
                onExportMileage = { vehicleID: String ->
                    navController.navigate(
                        ExportMileage(
                            vehicleID
                        )
                    )
                }
            )
        }
        composable<MileageDetail> {
            MileageScreen(
                onEditMileage = { mileageID: String ->
                    navController.navigate(
                        EditMileage(
                            vehicleID = "",
                            mileageID = mileageID
                        )
                    )
                },
                onHome = { navController.navigate(Home) }
            )
        }
        composable<EditVehicle> { EditVehicleScreen() }
        composable<EditMileage> { EditMileageScreen() }
        composable<ExportMileage> { ExportMileageScreen() }
    }
}
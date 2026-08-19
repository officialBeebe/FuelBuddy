package com.dylanbeebe.fuelbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dylanbeebe.fuelbuddy.ui.mileage.edit.EditMileageScreen
import com.dylanbeebe.fuelbuddy.ui.vehicle.EditVehicleScreen
import com.dylanbeebe.fuelbuddy.ui.mileage.export.ExportMileageScreen
import com.dylanbeebe.fuelbuddy.ui.home.HomeScreen
import com.dylanbeebe.fuelbuddy.ui.mileage.detail.MileageScreen
import com.dylanbeebe.fuelbuddy.ui.vehicle.VehicleScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                onVehicleClick = { vehicleID: String ->
                    navController.navigate(
                        VehicleDetail(
                            vehicleID
                        )
                    )
                },
                onAddVehicle = { navController.navigate(EditVehicle()) }
            )
        }
        composable<VehicleDetail> {
            VehicleScreen(
                onMileageClick = { mileageID: String ->
                    navController.navigate(
                        MileageDetail(
                            mileageID
                        )
                    )
                },
                onEditVehicle = { vehicleID: String -> navController.navigate(EditVehicle(vehicleID)) },
                onBack = { navController.popBackStack() },
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
                    navController.navigate(EditMileage(mileageID = mileageID))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<EditMileage> {
            EditMileageScreen(
                onBack = { navController.popBackStack() },
                onMileageDeleted = {
                    navController.popBackStack<VehicleDetail>(inclusive = false)
                },
            )
        }

        composable<EditVehicle> {
            EditVehicleScreen(
                onBack = { navController.popBackStack() },
                onVehicleDeleted = {
                    navController.popBackStack<Home>(inclusive = false)
                }
            )
        }
        composable<ExportMileage> {
            ExportMileageScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
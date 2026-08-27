package com.dylanbeebe.fuelbuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dylanbeebe.fuelbuddy.ui.core.mileage.add.MileageAddScreen
import com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailScreen
import com.dylanbeebe.fuelbuddy.ui.core.mileage.edit.MileageEditScreen
import com.dylanbeebe.fuelbuddy.ui.core.mileage.export.MileageExportScreen
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.edit.VehicleEditScreen
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.add.VehicleAddScreen
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.list.VehicleListScreen
import com.dylanbeebe.fuelbuddy.ui.core.vehicle.detail.VehicleDetailScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = VehicleList,
    ) {
        composable<VehicleList> {
            VehicleListScreen(
                onVehicleClick = { vehicleID: String ->
                    navController.navigate(
                        VehicleDetail(
                            vehicleID
                        )
                    )
                },
                // NOTE: Routes.kt now has a dedicated VehicleAdd object, but no
                // screen is wired to it yet — keeping this on VehicleEdit()
                // (nullable vehicleID, add-when-null) to match current
                // EditVehicleScreen behavior. Revisit once vehicle add is split out.
                onAddVehicle = { navController.navigate(VehicleAdd) }
            )
        }
        composable<VehicleAdd> {
            VehicleAddScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<VehicleDetail> {
            VehicleDetailScreen(
                onMileageClick = { mileageID: String ->
                    navController.navigate(
                        MileageDetail(
                            mileageID
                        )
                    )
                },
                onEditVehicle = { vehicleID: String -> navController.navigate(VehicleEdit(vehicleID)) },
                onBack = { navController.popBackStack() },
                onAddMileage = { vehicleID: String -> navController.navigate(MileageAdd(vehicleID)) },
                onExportMileage = { vehicleID: String ->
                    navController.navigate(
                        ExportMileage(
                            vehicleID
                        )
                    )
                }
            )
        }
        composable<VehicleEdit> {
            VehicleEditScreen(
                onBack = { navController.popBackStack() },
                onVehicleDeleted = {
                    navController.popBackStack<VehicleList>(inclusive = false)
                }
            )
        }
        composable<MileageDetail> {
            MileageDetailScreen(
                onEditMileage = { mileageID: String ->
                    navController.navigate(MileageEdit(mileageID))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<MileageAdd> {
            MileageAddScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable<MileageEdit> {
            MileageEditScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable<ExportMileage> {
            MileageExportScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
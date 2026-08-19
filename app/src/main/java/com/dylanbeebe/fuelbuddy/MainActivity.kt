package com.dylanbeebe.fuelbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dylanbeebe.fuelbuddy.ui.navigation.NavGraph
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import kotlin.time.ExperimentalTime

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // DEBUG
//        val app = application as FuelBuddyApplication
//        val vehicleRepository = app.vehicleRepositoryImpl
//        val mileageRepository = app.mileageRepositoryImpl

//        lifecycleScope.launch {
//            if (vehicleRepository.observeAllVehicles().first().isEmpty()) {
//                // Debug insert 1
//                val theKia = Vehicle(
//                        nickname = "The Kia",
//                        make = "Kia",
//                        model = "Soul",
//                        modelYear = 2019,
//                        plate = "IH8DIS1",
//                    )
//                vehicleRepository.insert(theKia)
//                vehicleRepository.addAttachment(
//                    VehicleAttachment(
//                        filePath = "/the/kia/test/attachment",
//                        vehicle = theKia.vehicleID
//                    )
//                )
//                val now = Clock.System.now()
//                val theKiaMileage1 = Mileage(
//                    timestamp = now.toString(),
//                    odometerMiles = 80085.69,
//                    volumeGallons = 6.9,
//                    isFullTank = true,
//                    fuelType = FuelType.REGULAR,
//                    totalDollars = 19.84,
//                    journal = "This is a test mileage log.",
//                    vehicle = theKia.vehicleID
//                )
//                mileageRepository.insert(theKiaMileage1)
//                mileageRepository.addAttachment(
//                    MileageAttachment(
//                        filePath = "/the/kia/test/attachment",
//                        mileage = theKiaMileage1.mileageID
//                    )
//                )
//
//                // Debug insert 2
//                vehicleRepository.insert(
//                    Vehicle(
//                        nickname = "Coop",
//                        make = "BMW",
//                        model = "Mini Cooper",
//                        modelYear = 2016,
//                        plate = "I<3DIS1",
//                    )
//                )
//            }
//        }

        setContent {
            FuelBuddyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
//                        .safeDrawingPadding(),
                ) {
                    NavGraph()
                }
            }
        }
    }
}


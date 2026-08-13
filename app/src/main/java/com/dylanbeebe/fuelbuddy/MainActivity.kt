package com.dylanbeebe.fuelbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dylanbeebe.fuelbuddy.data.model.FuelType
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.ui.navigation.NavGraph
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.URI
import java.time.LocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as FuelBuddyApplication
        val vehicleRepository = app.vehicleRepository
        val mileageRepository = app.mileageRepository

        // DEBUG
        lifecycleScope.launch {
            if (vehicleRepository.observeAllVehicles().first().isEmpty()) {
                // Debug insert 1
                val theKia = Vehicle(
                        nickname = "The Kia",
                        make = "Kia",
                        model = "Soul",
                        modelYear = 2019,
                        plate = "IH8DIS1",
                    )
                vehicleRepository.insert(theKia)
                vehicleRepository.addAttachment(
                    VehicleAttachment(
                        URI = "/the/kia/test/attachment",
                        vehicle = theKia.vehicleID
                    )
                )
                val now = Clock.System.now()
                val theKiaMileage1 = Mileage(
                    timestamp = now.toString(),
                    latitude = -48.876667,
                    longitude = -123.393333,
                    odometerMiles = 80085.69,
                    volumeGallons = 6.9,
                    isFullTank = true,
                    fuelType = FuelType.REGULAR,
                    totalDollars = 19.84,
                    journal = "This is a test mileage log.",
                    vehicle = theKia.vehicleID
                )
                mileageRepository.insert(theKiaMileage1)
                mileageRepository.addAttachment(
                    MileageAttachment(
                        URI = "/the/kia/test/attachment",
                        mileage = theKiaMileage1.mileageID
                    )
                )

                // Debug insert 2
                vehicleRepository.insert(
                    Vehicle(
                        nickname = "Coop",
                        make = "BMW",
                        model = "Mini Cooper",
                        modelYear = 2016,
                        plate = "I<3DIS1",
                    )
                )
            }
        }

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


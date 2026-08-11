package com.dylanbeebe.fuelbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.ui.navigation.NavGraph
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as FuelBuddyApplication
        val vehicleRepository = app.vehicleRepository

        // DEBUG
        lifecycleScope.launch {
            if (vehicleRepository.allVehicles().isEmpty()) {
                vehicleRepository.insert(
                    Vehicle(
                        nickname = "The Kia",
                        make = "Kia",
                        model = "Soul",
                        modelYear = 2019,
                        plate = "IH8DIS1",
                    )
                )
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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}


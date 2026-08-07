package com.dylanbeebe.fuelbuddy

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import com.dylanbeebe.fuelbuddy.ui.screen.HomeScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            FuelBuddyDB::class.java,
            "fuelbuddy-db"
        ).build()
        val vehicleRepository = VehicleRepository(db.vehicleDAO())

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
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(
                        vehicleRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// TODO: Implement Compose Navigation: https://developer.android.com/guide/navigation


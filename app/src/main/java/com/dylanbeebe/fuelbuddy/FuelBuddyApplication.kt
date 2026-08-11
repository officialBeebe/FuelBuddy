package com.dylanbeebe.fuelbuddy

import android.app.Application
import androidx.room.Room
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository

class FuelBuddyApplication : Application() {
    lateinit var db: FuelBuddyDB
    lateinit var vehicleRepository: VehicleRepository
    lateinit var mileageRepository: MileageRepository

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, FuelBuddyDB::class.java, "fuelbuddy-db").build()
        vehicleRepository = VehicleRepository(db.vehicleDAO())
        mileageRepository = MileageRepository(db.mileageDAO()) // adjust to your actual DAO
    }
}
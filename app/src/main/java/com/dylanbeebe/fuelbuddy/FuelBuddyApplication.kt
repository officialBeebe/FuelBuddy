package com.dylanbeebe.fuelbuddy

import android.app.Application
import androidx.room.Room
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryImpl

class FuelBuddyApplication : Application() {
    lateinit var db: FuelBuddyDB
    lateinit var vehicleRepositoryImpl: VehicleRepositoryImpl
    lateinit var mileageRepositoryImpl: MileageRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, FuelBuddyDB::class.java, "fuelbuddy-db").build()
        vehicleRepositoryImpl = VehicleRepositoryImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())
        mileageRepositoryImpl = MileageRepositoryImpl(db.mileageDAO(), db.mileageAttachmentDAO())
    }
}
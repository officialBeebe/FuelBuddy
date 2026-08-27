package com.dylanbeebe.fuelbuddy

import android.app.Application
import androidx.room.Room
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryRoomImpl

class FuelBuddyApplication : Application() {
    lateinit var db: FuelBuddyDB
    lateinit var vehicleRepositoryRoomImpl: VehicleRepositoryRoomImpl
    lateinit var mileageRepositoryRoomImpl: MileageRepositoryRoomImpl

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, FuelBuddyDB::class.java, "fuelbuddy-db").build()
        vehicleRepositoryRoomImpl = VehicleRepositoryRoomImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())
        mileageRepositoryRoomImpl = MileageRepositoryRoomImpl(db.mileageDAO(), db.mileageAttachmentDAO())
    }
}
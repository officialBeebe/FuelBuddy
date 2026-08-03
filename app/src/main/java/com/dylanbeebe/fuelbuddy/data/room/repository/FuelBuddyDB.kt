package com.dylanbeebe.fuelbuddy.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.dao.UserDAO
import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.User
import com.dylanbeebe.fuelbuddy.data.model.Vehicle

@Database(entities = [User::class, Vehicle::class, Mileage::class], version = 1)
abstract class FuelBuddyDB : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun vehicleDAO(): VehicleDAO
    abstract fun mileageDAO(): MileageDAO

    companion object {
        @Volatile
        private var Instance: FuelBuddyDB? = null

        fun getDatabase(context: Context): FuelBuddyDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FuelBuddyDB::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
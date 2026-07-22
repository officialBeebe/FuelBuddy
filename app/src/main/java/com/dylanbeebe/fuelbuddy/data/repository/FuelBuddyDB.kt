package com.dylanbeebe.fuelbuddy.data.repository

import androidx.room.Database
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

}
package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.dao.relations.VehicleMinimal
import com.dylanbeebe.fuelbuddy.data.dao.relations.UserAndAllVehicles

@Dao
interface VehicleDAO : BaseDao<Vehicle> {
    @Query("SELECT * FROM vehicle")
    fun getAll(): List<Vehicle>

    @Query("SELECT vehicleID, nickname, user FROM vehicle")
    fun getAllMinimal(): List<VehicleMinimal>

    @Transaction
    @Query("SELECT * FROM User")
    fun getUserAndAllVehicles(): List<UserAndAllVehicles>
}
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
    suspend fun getAll(): List<Vehicle>

    @Query("SELECT vehicleID, nickname, user FROM vehicle")
    suspend fun getAllMinimal(): List<VehicleMinimal>

    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getUserAndAllVehicles(): List<UserAndAllVehicles>
}